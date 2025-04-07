package ice.private.brynj.service

import ice.private.brynj.model.*
import ice.private.brynj.database.entities.*
import ice.private.brynj.model.Amenity
import ice.private.brynj.model.Hotel
import org.jetbrains.exposed.sql.transactions.transaction
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


object HotelService {

    private val cache = LinkedHashSet<Hotel>()
    private fun loadHotelsIntoCache(limit: Int) = transaction { cache.addAll(HotelEntity.all().limit(limit).map { it.toDto() }) }


    fun searchHotels(criteria: SearchCriteria): List<Hotel> {

        /** byrjum á að leita í in-memory cache
        *  .filter er 'Higher-Order Function', það tekur lista (eins og Cache) og iteratar í gegnum hann
        *  þar sem '{ hotel -> ' táknar eitt 'item' í listanum. allt sem er innan { } virkar svipar
        *  og for lykkja, þar sem ef lokagildið er 'true', þá bætist 'itemið' við í nýja listann sem .filter býr til.*/
        val resultList = cache.filter { hotel ->

            /** Ef 'city' er null, þá skilar það true. Annars athugar það hvort 'hotel'.city er jafnt of 'criteria'.city
            *  hotelið verður ekki bætt við í lokalistann ef það fær 'false', þess vegna fá öll hótel 'true'
            *  ef criteriaði tilgreinir ekki 'city'                  */
            val matchesCity = criteria.city?.equals(hotel.city, ignoreCase = true) ?: true

            /** kannar hvort 'hotel'.starRating sé fyrir utan criteria mörkin, og skilar 'true' ef svo er ekki. */
            val matchesStarRating = when {
                /* minStar ekki null OG 'hotel'.starRating er minna en lágmarks krafa => false (hótel fer ekki í matchLista) */
                criteria.minStarRating != null && hotel.starRating < criteria.minStarRating!! -> false
                /* maxStar ekki null OG 'hotel'.starRating er hærra en hágmarks krafa => false (hótel fer ekki í matchLista) */
                criteria.maxStarRating != null && hotel.starRating > criteria.maxStarRating!! -> false
                else -> true    // annars fer það í listann
            }

            /** ef 'criteria'.selectedAmenities er ekki tómur */
            val matchesAmenities = if (criteria.selectedAmenities.isNotEmpty()) {
                criteria.selectedAmenities.all { it in hotel.amenities }
            } else true

            /** kannar fyrst hvort criteria tilgreinir annaðhvort minPrice eða maxPrice  */
            val matchesPrice = if (criteria.minPricePerNight != null || criteria.maxPricePerNight != null) {

                /* null safety */
                val hotelMin = hotel.minPrice!!
                val hotelMax = hotel.maxPrice!!

                /* ef annar þeirra var null, þá fær það gildi 'extreme' endann af því sem er mögulegt, þ.e. eins og að setja enginn efri/nerði mörk */
                val userMin = criteria.minPricePerNight ?: 0
                val userMax = criteria.maxPricePerNight ?: Int.MAX_VALUE

                /* ef hotelMax eða hotelMix eru innan criteriaMax og criteriaMin öðrum megin, þá true, annars false */
                !(hotelMax < userMin || hotelMin > userMax)
            } else true

            /*  */
            val matchesDate = if (criteria.fromDate != null && criteria.toDate != null) {
                TODO()
            } else true


            /* þetta expression ákveður hvort 'hotelið' sem við erum á núna fari í lokalistann */
            matchesCity && matchesStarRating && matchesAmenities && matchesPrice && matchesDate
        }


        /* skila result ef það fannst í cache */
        if (resultList.isNotEmpty()) return resultList


    }




    fun createHotel() {}
    fun deleteHotel() {}
}

