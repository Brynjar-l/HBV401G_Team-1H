package database.entities


import database.tables.BookingTable
import model.Booking
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate

class BookingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BookingEntity>(BookingTable)

    var room by RoomEntity referencedOn BookingTable.room
    var fromDate by BookingTable.fromDate
    var toDate by BookingTable.toDate
    var totalPrice by BookingTable.totalPrice


    fun toDto(): Booking {

        val fromDate: LocalDate = LocalDate.parse(this.fromDate)
        val toDate: LocalDate = LocalDate.parse(this.toDate)

        return Booking(
            id = this.id.value,
            roomId = this.room.id.value,

            fromDate = fromDate,
            toDate = toDate,
            totalPrice = this.totalPrice,
        )
    }
}