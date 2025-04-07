package ice.private.brynj.model

data class Hotel(

    val name: String,
    val address: String,
    val city: String,
    val starRating: Double,
    val description: String? = null,

    val id: Int? = null,

    val rooms: List<Room> = emptyList(),
    val amenities: List<Amenity> = emptyList(),

    var minPrice: Int? = null,
    var maxPrice: Int? = null,
)
