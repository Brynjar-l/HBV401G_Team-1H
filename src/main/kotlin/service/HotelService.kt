package ice.private.brynj.service

import database.exposed.tables.AmenityTable
import ice.private.brynj.database.model.Amenity
import ice.private.brynj.database.model.Hotel
import java.time.LocalDate

object HotelService {

    private val cache: LinkedHashSet<Hotel> = linkedSetOf()
    private fun loadToMemory(sizeLimit: Int) {}


    fun query(): HotelQueryBuilder {
        return HotelQueryBuilder()
    }

    private data class HotelQueryBuilder(
        var id: Int? = null,
        var name: String? = null,
        var city: String? = null,
        var address: String? = null,

        var location: String? = null,   // city + address aggregate

        var priceRange: IntRange? = null,
        var dateRange: ClosedRange<LocalDate>? = null,

        var amenities: MutableSet<Amenity>? = null,
    ) {

    }
}


