package service

import model.Amenity
import utils.AmenityProvider
import utils.defaultAmenityProvider


class AmenityService(
    private val amenityProvider: AmenityProvider = defaultAmenityProvider()
) {
    fun loadAllAmenities(): List<Amenity> {
        return amenityProvider()
    }
}