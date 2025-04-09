package utils

import model.Amenity
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

