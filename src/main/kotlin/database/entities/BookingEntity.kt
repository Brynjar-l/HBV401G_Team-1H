package ice.private.brynj.database.entities


import ice.private.brynj.database.tables.BookingTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BookingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BookingEntity>(BookingTable)

    var room by RoomEntity referencedOn BookingTable.room
    var fromDate by BookingTable.fromDate
    var toDate by BookingTable.toDate
    var totalPrice by BookingTable.totalPrice
}