package database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object HotelTable : IntIdTable("hotels") {
    val name = varchar("hotel_name", 255)
    val address = varchar("hotel_address", 255)
    val city = varchar("hotel_city", 255)
    val starRating = double("star_rating").check { it.between(0.0, 5.0) }
    val description = text("description").nullable()
}