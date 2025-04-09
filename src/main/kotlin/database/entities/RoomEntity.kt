package database.entities


import database.tables.BookingTable
import database.tables.RoomTable
import model.Room

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate


class RoomEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomEntity>(RoomTable)

    /* many-to-one */
    var hotel by HotelEntity referencedOn RoomTable.hotel

    var roomNumber by RoomTable.roomNumber
    var pricePerNight by RoomTable.pricePerNight
    var numberOfBeds by RoomTable.numberOfBeds

    val bookings by BookingEntity referrersOn BookingTable.room

    fun toDto(): Room {

        val bookedDates: List<Pair<LocalDate, LocalDate>> = this.bookings.map { it.toDto() }.map { Pair(it.fromDate, it.toDate) }

        return Room(
            id = this.id.value,
            roomNumber = this.roomNumber,
            pricePerNight = this.pricePerNight,
            hotelId = this.hotel.id.value,
            numberOfBeds = this.numberOfBeds,
            bookedDates = bookedDates,
        )
    }
}

