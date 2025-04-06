package ice.private.brynj.service

import ice.private.brynj.database.model.*
import ice.private.brynj.database.entities.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

data class SearchCriteria(
    var selectedAmenities: Set<Amenity> = emptySet(),
    var city: String? = null,
    var minStarRating: Double? = null,
    var maxStarRating: Double? = null,
    var minPricePerNight: Int? = null,
    var maxPricePerNight: Int? = null,
    var fromDate: LocalDate? = null,
    var toDate: LocalDate? = null,
    var numberOfBeds: Int? = null,
)


object HotelService {

    private val cache = LinkedHashSet<Hotel>()
    private fun loadHotelsIntoCache(limit: Int) = transaction { cache.addAll(HotelEntity.all().limit(limit).map { it.toDto() }) }


    fun searchHotels(criteria: SearchCriteria): List<Hotel> {

        val resultList = cache.filter { hotel ->
            // city filter
            val matchesCity = criteria.city?.equals(hotel.city, ignoreCase = true) ?: true

            // star rating filter
            val matchesStarRating = when {
                criteria.minStarRating != null && hotel.starRating < criteria.minStarRating!! -> false
                criteria.maxStarRating != null && hotel.starRating > criteria.maxStarRating!! -> false
                else -> true
            }

            // amenities filter
            val matchesAmenities = if (criteria.selectedAmenities.isNotEmpty()) {
                criteria.selectedAmenities.all { it in hotel.amenities }
            } else true

            // price filter based on precomputed minPricePerNight / maxPricePerNight
            val matchesPrice = if (criteria.minPricePerNight != null || criteria.maxPricePerNight != null) {
                val hotelMin = hotel.minPrice ?: 0
                val hotelMax = hotel.maxPrice ?: Int.MAX_VALUE
                val userMin = criteria.minPricePerNight ?: 0
                val userMax = criteria.maxPricePerNight ?: Int.MAX_VALUE

                // check if the hotel's price range overlaps the user's desired price range
                !(hotelMax < userMin || hotelMin > userMax)
            } else true

            // date availability check
            val matchesDate = if (criteria.fromDate != null && criteria.toDate != null) {
                // For in-memory approach, you'd check if the hotel has at least one room
                // without an overlapping booking in [fromDate, toDate].
                // We'll assume a helper method:
                hasAvailability(hotel, criteria.fromDate!!, criteria.toDate!!)
            } else true

            matchesCity && matchesStarRating && matchesAmenities && matchesPrice && matchesDate
        }

        if (resultList.isNotEmpty()) return resultList


    }
    private fun hasAvailability(hotel: Hotel, fromDate: LocalDate, toDate: LocalDate): Boolean {
        // Example logic: if ANY of the hotel's rooms is unbooked in [fromDate..toDate], hotel is "available".
        return hotel.rooms.any { room ->
            room.bookedDateRanges.none { range ->
                range.start <= toDate && range.endInclusive >= fromDate
            }
        }
    }


    fun createHotel() {}
    fun deleteHotel() {}
}

