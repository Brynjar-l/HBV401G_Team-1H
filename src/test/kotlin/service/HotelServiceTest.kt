package service

import utils.SearchCriteria
import model.Amenity
import model.Hotel
import model.Room
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class HotelServiceTest {

    private lateinit var service: HotelService

    @BeforeEach
    fun setup() {
        service = HotelService { testHotels }
    }

    private val testHotels = listOf(
        Hotel(
            id = 1,
            name = "Hotel A",
            city = "Reykjavik",
            starRating = 4.5,
            address = "Sæmundargata 1",
            amenities = listOf(Amenity("free-wifi"), Amenity("parking")),
            rooms = listOf(
                Room(
                    id = 1,
                    hotelId = 1,
                    roomNumber = "101",
                    pricePerNight = 150,
                    numberOfBeds = 2,
                    bookedDates = listOf(
                        LocalDate.of(2025, 5, 1) to LocalDate.of(2025, 5, 3),
                        LocalDate.of(2025, 5, 15) to LocalDate.of(2025, 5, 20)
                    ),
                    )
            ),
        ),

        Hotel(
            id = 2,
            name = "Hotel B",
            city = "Kopavogur",
            starRating = 2.5,
            address = "Sæmundargata 2",
            amenities = listOf(Amenity("free-wifi"), Amenity("parking")),
            rooms = listOf(
                Room(
                    id = 2,
                    hotelId = 2,
                    roomNumber = "101",
                    pricePerNight = 350,
                    numberOfBeds = 2,
                    bookedDates = listOf(
                        LocalDate.of(2025, 6, 1) to LocalDate.of(2025, 5, 3),
                        LocalDate.of(2025, 6, 7) to LocalDate.of(2025, 11, 20)
                    ),
                    )
            ),
        )
    )




    @Test
    fun `search should return hotel matching city and date availability`() {
        val criteria = SearchCriteria(
            city = "Reykjavik",
            fromDate = LocalDate.of(2025, 5, 5),
            toDate = LocalDate.of(2025, 5, 8)
        )

        val result = service.searchHotels(criteria)

        assertEquals(1, result.size)
        assertEquals("Hotel A", result.first().name)
    }

    @Test
    fun `search should exclude hotel if room is booked`() {
        val criteria = SearchCriteria(
            city = "Reykjavik",
            fromDate = LocalDate.of(2025, 5, 2),
            toDate = LocalDate.of(2025, 5, 4)
        )

        val result = service.searchHotels(criteria)

        assertTrue(result.isEmpty())
    }
    
    @Test
    fun `search should return `() {
        
    }

    @Nested
    inner class DateValidation {


    }
    

}