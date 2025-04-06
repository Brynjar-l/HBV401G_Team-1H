package ice.private.brynj.database.model

data class Hotel(

    val name: String,
    val address: String,
    val city: String,
    val starRating: Double,
    val description: String? = null,

    val id: Int? = null,

    val rooms: MutableList<Room> = mutableListOf(),
    val amenities: MutableList<Amenity> = mutableListOf(),

    var minPrice: Int? = null,
    var maxPrice: Int? = null,
)
