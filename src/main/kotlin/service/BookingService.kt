package service

import database.entities.BookingEntity
import database.entities.RoomEntity
import database.tables.BookingTable
import model.Booking
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class BookingService {

    fun createBooking(roomId: Int, fromDate: LocalDate, toDate: LocalDate): Booking {
        return transaction {

            // kanna overlap
            val overlapCount = BookingEntity.find {
                (BookingTable.room eq roomId) and
                        (BookingTable.fromDate lessEq toDate.toString()) and
                        (BookingTable.toDate greaterEq fromDate.toString())
            }.count()
            if (overlapCount > 0) {
                throw IllegalStateException("Room $roomId is already booked from $fromDate to $toDate.")
            }

            // heildarverð
            val nights = ChronoUnit.DAYS.between(fromDate, toDate).toInt()
            if (nights <= 0) {
                throw IllegalArgumentException("Check-out date must be after check-in date.")
            }
            val roomEntity = RoomEntity[roomId]
            val totalPrice = nights * roomEntity.pricePerNight


            // nýtt booking
            val newBooking = BookingEntity.new {
                this.room = roomEntity
                this.fromDate = fromDate.toString()
                this.toDate = toDate.toString()
                this.totalPrice = totalPrice
            }

            // skila sem dto
            newBooking.toDto()
        }
    }
}
