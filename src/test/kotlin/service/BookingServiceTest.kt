package service

import database.entities.BookingEntity
import database.entities.HotelEntity
import database.entities.RoomEntity
import database.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

class BookingServiceTest {

    private val bookingService = BookingService()

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupDatabase() {
            Database.connect("jdbc:h2:mem:test_booking;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
            transaction {
                SchemaUtils.createMissingTablesAndColumns(
                    HotelTable,
                    RoomTable,
                    AmenityTable,
                    BookingTable,
                    HotelAmenitiesTable
                )
            }
        }
    }

    @BeforeEach
    fun clearData() {
        transaction {
            BookingEntity.all().forEach { it.delete() }
            RoomEntity.all().forEach { it.delete() }
            HotelEntity.all().forEach { it.delete() }
        }
    }

    @Test
    fun noOverlap_createsBookingSuccessfully() {
        val roomId = transaction {
            val hotel = HotelEntity.new {
                name = "Test Hotel"
                address = "123 Street"
                city = "TestCity"
                starRating = 4.0
                description = "Test Desc"
            }
            val room = RoomEntity.new {
                this.hotel = hotel
                roomNumber = "101"
                pricePerNight = 5000
                numberOfBeds = 2
            }
            room.id.value
        }
        val from = LocalDate.of(2025, 5, 1)
        val to = LocalDate.of(2025, 5, 5)
        val booking = bookingService.createBooking(roomId, from, to)
        assertNotNull(booking.id)
        assertEquals(roomId, booking.roomId)
        val nights = ChronoUnit.DAYS.between(from, to).toInt()
        assertEquals(nights * 5000, booking.totalPrice)
        transaction {
            assertEquals(1, BookingEntity.all().count())
        }
    }

    @Test
    fun overlappingDates_throwsRoomUnavailableException() {
        val roomId = transaction {
            val hotel = HotelEntity.new {
                name = "Overlap Hotel"
                address = "Overlap St."
                city = "Overlap City"
                starRating = 3.0
                description = null
            }
            val room = RoomEntity.new {
                this.hotel = hotel
                roomNumber = "201"
                pricePerNight = 4000
                numberOfBeds = 2
            }
            BookingEntity.new {
                this.room = room
                fromDate = "2025-06-01"
                toDate = "2025-06-05"
                totalPrice = 4 * 4000
            }
            room.id.value
        }
        try {
            bookingService.createBooking(
                roomId,
                LocalDate.of(2025, 6, 4),
                LocalDate.of(2025, 6, 7)
            )
            fail("Expected RoomUnavailableException")
        } catch (ex: IllegalStateException) {
            assertTrue(ex.message!!.contains("already booked"))
        }
    }

    @Test
    fun invalidDateRange_throwsIllegalArgumentException() {
        val roomId = transaction {
            val hotel = HotelEntity.new {
                name = "Invalid Range Hotel"
                address = "Bad date st."
                city = "BadCity"
                starRating = 4.0
                description = null
            }
            RoomEntity.new {
                this.hotel = hotel
                roomNumber = "301"
                pricePerNight = 3000
                numberOfBeds = 1
            }.id.value
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            bookingService.createBooking(
                roomId,
                LocalDate.of(2025, 7, 10),
                LocalDate.of(2025, 7, 9)
            )
        }
    }

    @Test
    fun multipleExistingBookings_catchesPartialOverlap() {
        val roomId = transaction {
            val hotel = HotelEntity.new {
                name = "MultiBooking Hotel"
                address = "Multi st."
                city = "MultiCity"
                starRating = 5.0
                description = null
            }
            val room = RoomEntity.new {
                this.hotel = hotel
                roomNumber = "401"
                pricePerNight = 6000
                numberOfBeds = 2
            }
            BookingEntity.new {
                this.room = room
                fromDate = "2025-08-01"
                toDate = "2025-08-03"
                totalPrice = 2 * 6000
            }
            BookingEntity.new {
                this.room = room
                fromDate = "2025-08-05"
                toDate = "2025-08-07"
                totalPrice = 2 * 6000
            }
            room.id.value
        }
        Assertions.assertThrows(IllegalStateException::class.java) {
            bookingService.createBooking(
                roomId,
                LocalDate.of(2025, 8, 2),
                LocalDate.of(2025, 8, 6)
            )
        }
    }

    @Test
    fun multipleExistingBookings_noOverlap_createsSuccessfully() {
        val roomId = transaction {
            val hotel = HotelEntity.new {
                name = "MultiBooking Hotel"
                address = "Multi st."
                city = "MultiCity"
                starRating = 5.0
                description = null
            }
            val room = RoomEntity.new {
                this.hotel = hotel
                roomNumber = "401"
                pricePerNight = 6000
                numberOfBeds = 2
            }
            BookingEntity.new {
                this.room = room
                fromDate = "2025-08-01"
                toDate = "2025-08-03"
                totalPrice = 2 * 6000
            }
            BookingEntity.new {
                this.room = room
                fromDate = "2025-08-05"
                toDate = "2025-08-07"
                totalPrice = 2 * 6000
            }
            room.id.value
        }
        val booking = bookingService.createBooking(
            roomId,
            LocalDate.of(2025, 8, 3),
            LocalDate.of(2025, 8, 5)
        )
        assertNotNull(booking.id)
        transaction {
            assertEquals(3, BookingEntity.all().count())
        }
    }
}
