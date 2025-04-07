package ice.private.brynj.model

data class Room(

    val roomNumber: String? = null,
    val pricePerNight: Int,
    val numberOfBeds: Int,

    val id: Int? = null,
    val hotelId: Int
)
