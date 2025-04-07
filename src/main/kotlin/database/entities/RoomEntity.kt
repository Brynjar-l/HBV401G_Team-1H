package ice.private.brynj.database.entities


import ice.private.brynj.database.tables.BookingTable
import ice.private.brynj.database.tables.RoomTable
import ice.private.brynj.model.Room

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class RoomEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomEntity>(RoomTable)

    /* many-to-one */
    var hotel by HotelEntity referencedOn RoomTable.hotel

    var roomNumber by RoomTable.roomNumber
    var pricePerNight by RoomTable.pricePerNight
    var numberOfBeds by RoomTable.numberOfBeds

    val bookings by BookingEntity referrersOn BookingTable.room

    fun toDto(): Room = Room(
        id = this.id.value,
        roomNumber = this.roomNumber,
        pricePerNight = this.pricePerNight,
        hotelId = this.hotel.id.value,
        numberOfBeds = this.numberOfBeds
    )
}

