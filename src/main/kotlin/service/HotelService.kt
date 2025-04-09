package service

import database.entities.HotelEntity
import model.Hotel
import utils.SearchCriteria
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate


typealias HotelProvider = () -> List<Hotel>

fun defaultProvider(): HotelProvider = {
    transaction {
        HotelEntity.all().map { it.toDto() }
    }
}

class HotelService(private val hotelProvider: HotelProvider = defaultProvider()) {

    private val cache = LinkedHashSet<Hotel>()

    init {
        refreshCache(5000)
    }

    fun searchHotels(criteria: SearchCriteria): List<Hotel> {
        val resultList = queryCache(criteria)

        if (resultList.isNotEmpty()) return resultList
        else refreshCache()

        return queryCache(criteria)
    }


    private fun queryCache(criteria: SearchCriteria): List<Hotel> = cache.filter { hotel ->

        val matchesAmenities = Match.amenities(hotel, criteria)
        val matchesCity = Match.city(hotel, criteria)
        val matchesStarRating = Match.starRating(hotel, criteria)
        val matchesPrice = Match.price(hotel, criteria)
        val matchesDate = Match.date(hotel, criteria)

        matchesCity && matchesStarRating && matchesAmenities && matchesPrice && matchesDate
    }

    private object Match {
        fun amenities(hotel: Hotel, criteria: SearchCriteria): Boolean {
        if (criteria.selectedAmenities.isNotEmpty()) {
            // Ensure the hotel contains all of the selected amenities
            return criteria.selectedAmenities.all { selectedAmenity ->
                hotel.amenities.any { it.name == selectedAmenity.name }
            }
        }
        return true // If no amenities are selected, return true (all hotels are considered a match)
        }


        fun city(hotel: Hotel, criteria: SearchCriteria): Boolean {
            return criteria.city?.equals(hotel.city, ignoreCase = true) ?: true
        }

        fun starRating(hotel: Hotel, criteria: SearchCriteria): Boolean {
            return when {
                criteria.minStarRating != null && hotel.starRating < criteria.minStarRating!! -> false
                criteria.maxStarRating != null && hotel.starRating > criteria.maxStarRating!! -> false
                else -> true
            }
        }

        fun price(hotel: Hotel, criteria: SearchCriteria): Boolean {
            return if (criteria.minPricePerNight != null || criteria.maxPricePerNight != null) {
                val hotelMin = hotel.minPrice!!
                val hotelMax = hotel.maxPrice!!

                val userMin = criteria.minPricePerNight ?: 0
                val userMax = criteria.maxPricePerNight ?: Int.MAX_VALUE

                !(hotelMax < userMin || hotelMin > userMax)
            } else true
        }

        fun date(hotel: Hotel, criteria: SearchCriteria): Boolean {
            return if (criteria.fromDate != null && criteria.toDate != null) {
                hotelCheckAvailability(hotel, criteria.fromDate!!, criteria.toDate!!)
            } else true
        }

        // fun validateNumberOfBeds()
        private fun hotelCheckAvailability(hotel: Hotel, fromDate: LocalDate, toDate: LocalDate): Boolean {
            return hotel.rooms.any { room ->
                val isOverlapping = room.bookedDates?.any { (bookedFromDate, bookedToDate) ->
                    fromDate <= bookedToDate && toDate >= bookedFromDate
                } ?: false

                !isOverlapping
            }
        }
    }

    private fun refreshCache(sizeLimit: Int = 5000) {
        cache.clear()
        cache.addAll(hotelProvider())
    }
}