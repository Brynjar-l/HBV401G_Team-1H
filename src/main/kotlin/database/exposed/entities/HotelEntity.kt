package database.exposed.entities


import database.exposed.tables.HotelAmenitiesTable
import database.exposed.tables.HotelTable
import database.exposed.tables.RoomTable
import ice.private.brynj.database.model.Hotel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class HotelEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<HotelEntity>(HotelTable)

    var name by HotelTable.name
    var address by HotelTable.address
    var city by HotelTable.city
    var starRating by HotelTable.starRating
    var description by HotelTable.description

    /* one-to-many */
    val rooms by RoomEntity referrersOn RoomTable.hotel
    var amenities by AmenityEntity via HotelAmenitiesTable


    fun toDto(): Hotel = Hotel(
        id = this.id.value,
        name = this.name,
        address = this.address,
        city = this.city,
        starRating = this.starRating,
        description = this.description,
        rooms = this.rooms.map { it.toDto() }.toMutableSet(),
        amenities = this.amenities.map { it.toDto() }.toMutableSet(),
    )

    val dto: Hotel
        get() = Hotel(
            id = this.id.value,
            name = this.name,
            address = this.address,
            city = this.city,
            starRating = this.starRating,
            description = this.description,
            rooms = this.rooms.map { it.dto }.toMutableSet(),
            amenities = this.amenities.map { it.dto }.toMutableSet(),
        )
}



