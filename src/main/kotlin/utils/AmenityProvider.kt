package utils

import database.entities.AmenityEntity
import model.Amenity
import org.jetbrains.exposed.sql.transactions.transaction

typealias AmenityProvider = () -> List<Amenity>

fun defaultAmenityProvider(): AmenityProvider = {
    transaction {
        AmenityEntity.all().map { it.toDto() }
    }
}