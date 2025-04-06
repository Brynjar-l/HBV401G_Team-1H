package database.exposed.entities

import database.exposed.tables.AmenityTable
import database.exposed.tables.HotelAmenitiesTable
import ice.private.brynj.database.model.Amenity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AmenityEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AmenityEntity>(AmenityTable)

    var name by AmenityTable.name
    var hotels by HotelEntity via HotelAmenitiesTable


    fun toDto(): Amenity = Amenity(
        id = this.id.value,
        name = this.name,
    )

    val dto: Amenity
        get() = Amenity(
        id = this.id.value,
        name = this.name,
    )
}