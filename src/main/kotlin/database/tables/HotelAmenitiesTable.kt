package ice.private.brynj.database.tables


import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object HotelAmenitiesTable : Table("hotel_amenities") {
    val hotel = reference("hotel_id", HotelTable, onDelete = ReferenceOption.CASCADE)
    val amenity = reference("amenity_id", AmenityTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(hotel, amenity)
}