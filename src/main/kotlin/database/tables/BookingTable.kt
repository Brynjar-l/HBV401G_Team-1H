package database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object BookingTable : IntIdTable("bookings", "booking_id") {
    val room = reference("room_id", RoomTable, onDelete = ReferenceOption.CASCADE)
    val fromDate = text("from_date")
    val toDate = text("to_date")
    val totalPrice = integer("total_price")
}