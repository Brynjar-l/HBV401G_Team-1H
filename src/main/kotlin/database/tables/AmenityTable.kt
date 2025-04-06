package ice.private.brynj.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object AmenityTable : IntIdTable("amenities", "id") {
    val name = varchar("amenity_name", 255).uniqueIndex()
}