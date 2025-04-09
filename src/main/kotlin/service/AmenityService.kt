package service

import database.entities.AmenityEntity
import model.Amenity
import org.jetbrains.exposed.sql.transactions.transaction
import utils.AmenityProvider
import utils.defaultAmenityProvider


class AmenityService(
    private val amenityProvider: AmenityProvider = defaultAmenityProvider()
) {
    fun loadAllAmenities(): List<Amenity> {
        return amenityProvider()
    }
}