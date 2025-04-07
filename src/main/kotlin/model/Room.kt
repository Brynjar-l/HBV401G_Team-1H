package ice.private.brynj.model

import java.time.LocalDate

data class Room(

    val roomNumber: String? = null,
    val pricePerNight: Int,
    val numberOfBeds: Int,

    val id: Int? = null,
    val hotelId: Int,

    val bookedDates: List<Pair<LocalDate, LocalDate>>? = null,
)
