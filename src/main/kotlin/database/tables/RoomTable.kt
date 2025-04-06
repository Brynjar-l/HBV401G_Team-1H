package ice.private.brynj.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object RoomTable : IntIdTable("rooms", "id") {
    val hotel = reference("hotel_id", HotelTable)
    val roomNumber = text("room_number").nullable()
    val pricePerNight = integer("room_price_per_night")
}