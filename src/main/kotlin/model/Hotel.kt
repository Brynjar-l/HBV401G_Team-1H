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

    ) {

    val minPrice: Int?
        get() = try {
            rooms.minOf { it.pricePerNight }
        } catch (_: NoSuchElementException) {
            null
        }


    val maxPrice: Int?
        get() = try {
            rooms.maxOf { it.pricePerNight }
        } catch (_: NoSuchElementException) {
            null
        }
}