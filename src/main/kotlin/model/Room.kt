package model

import java.time.LocalDate

data class Room(

    val roomNumber: String,
    val pricePerNight: Int,
    val numberOfBeds: Int,

    val id: Int = -1,
    val hotelId: Int,

    val bookedDates: List<Pair<LocalDate, LocalDate>> = emptyList(),
)
