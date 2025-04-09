package service

import database.entities.AmenityEntity
import model.Amenity
import org.jetbrains.exposed.sql.transactions.transaction

typealias AmenityProvider = () -> List<Amenity>

fun defaultAmenityProvider(): AmenityProvider = {
    transaction {
        AmenityEntity.all().map { it.toDto() }
    }
}

class AmenityService(
    private val amenityProvider: AmenityProvider = defaultAmenityProvider()
) {
    fun loadAllAmenities(): List<Amenity> {
        return amenityProvider()
    }
}