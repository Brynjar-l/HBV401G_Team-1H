package ice.private.brynj.database.model

data class Room(

    val roomNumber: String? = null,
    val pricePerNight: Int,
    /* val numberOfRooms: Int, */

    val id: Int? = null,
    val hotelId: Int
)
