package database.tables

import database.tables.HotelTable
import org.jetbrains.exposed.dao.id.IntIdTable

object RoomTable : IntIdTable("rooms", "id") {
    val hotel = reference("hotel_id", HotelTable)
    val roomNumber = text("room_number")
    val pricePerNight = integer("room_price_per_night")
    val numberOfBeds = integer("number_of_beds")
}