package service

import model.Hotel
import model.Room
import utils.SearchCriteria
import utils.HotelProvider
import utils.defaultProvider


class HotelService(private val hotelProvider: HotelProvider = defaultProvider()) {

    private val cache = LinkedHashSet<Hotel>()

    init { refreshCache() }


    fun searchHotels(criteria: SearchCriteria): List<Hotel> {

        val hotelFiltered = cache.filter { matchHotelLevelFilters(it, criteria) }

        val finalResults = hotelFiltered.mapNotNull { hotel ->
            val matchedRooms = hotel.rooms.filter { matchRoomLevelFilters(it, criteria) }
            if (matchedRooms.isEmpty()) {
                null
            } else {
                hotel.copy(rooms = matchedRooms)
            }
        }

        return finalResults
    }


    fun refreshCache() {
        cache.clear()
        cache.addAll(hotelProvider())
    }


    private fun matchHotelLevelFilters(hotel: Hotel, criteria: SearchCriteria): Boolean {

        if (!matchCity(hotel, criteria)) return false
        if (!matchAmenities(hotel, criteria)) return false
        if (!matchStarRating(hotel, criteria)) return false

        return true
    }

    private fun matchCity(hotel: Hotel, criteria: SearchCriteria): Boolean {

        val city = criteria.city
        return if (city != null) {
            hotel.city.equals(city, ignoreCase = true)
        } else {
            true
        }
    }

    private fun matchAmenities(hotel: Hotel, criteria: SearchCriteria): Boolean {
        val desired = criteria.selectedAmenities
        if (desired.isEmpty()) return true

        return desired.all { selected ->
            hotel.amenities.any { it.name.equals(selected.name, ignoreCase = true) }
        }
    }

    private fun matchStarRating(hotel: Hotel, criteria: SearchCriteria): Boolean {
        val minRating = criteria.minStarRating
        val maxRating = criteria.maxStarRating

        if (minRating != null && hotel.starRating < minRating) return false
        if (maxRating != null && hotel.starRating > maxRating) return false

        return true
    }

    private fun matchRoomLevelFilters(room: Room, criteria: SearchCriteria): Boolean {

        if (!matchPrice(room, criteria)) return false
        if (!matchBeds(room, criteria)) return false
        if (!matchAvailability(room, criteria)) return false

        return true
    }

    private fun matchPrice(room: Room, criteria: SearchCriteria): Boolean {

        val userMin = criteria.minPricePerNight ?: Int.MIN_VALUE
        val userMax = criteria.maxPricePerNight ?: Int.MAX_VALUE

        return room.pricePerNight in userMin..userMax
    }

    private fun matchBeds(room: Room, criteria: SearchCriteria): Boolean {

        val requiredBeds = criteria.numberOfBeds ?: return true
        return room.numberOfBeds >= requiredBeds
    }

    private fun matchAvailability(room: Room, criteria: SearchCriteria): Boolean {
        val from = criteria.fromDate
        val to = criteria.toDate

        if (from == null || to == null) return true

        val overlap = room.bookedDates.any { (bookedFrom, bookedTo) ->
            from <= bookedTo && to >= bookedFrom
        }

        return !overlap
    }
}
