package ice.private.brynj.service


//object HotelService {
//
//    private val cache: LinkedHashSet<Hotel> = linkedSetOf()
//    private fun loadToMemory(sizeLimit: Int) {}
//
//    inline fun queryBuilder(block: HotelSearchBuilder.() -> Unit): HotelQuery {
//        return HotelSearchBuilder().apply(block).build()
//    }
//
//
//    class HotelSearchBuilder {
//        fun build() = HotelQuery(
//            id = this.id,
//            name = this.name.value,
//            city = this.city.value,
//            address = this.address.value,
//
//            fromRating = this.rating.value?.start,
//            toRating = this.rating.value?.endInclusive,
//
//            fromPrice = this.price.value?.start,
//            toPrice = this.price.value?.endInclusive,
//
//            fromDate = this.fromDate.value,
//            toDate = this.toDate.value,
//            amenities = this.amenities
//        )
//
//        data class NamedValue<T>(val name: String, var value: T?)
//
//        val id: Int? = null
//        val name: NamedValue<String> = NamedValue("name", null)
//        val city: NamedValue<String> = NamedValue("city", null)
//        val address: NamedValue<String> = NamedValue("address", null)
//
//        val rating: NamedValue<ClosedRange<Double>> = NamedValue("starRatingRange", null)
//        val price: NamedValue<ClosedRange<Int>> = NamedValue("priceRange", null)
//
//
//        val fromDate: NamedValue<LocalDate> = NamedValue("fromDate", null)
//        val toDate: NamedValue<LocalDate> = NamedValue("toDate", null)
//        val amenities: MutableSet<Amenity>? = null
//
//
//        infix fun NamedValue<String>.like(other: String) {
//            when (this.name) {
//                "name" -> {
//                    this@HotelSearchBuilder.name.value = other
//                }
//
//                "city" -> {
//                    this@HotelSearchBuilder.city.value = other
//                }
//
//                "address" -> {
//                    this@HotelSearchBuilder.address.value = other
//                }
//            }
//        }
//
//
//        inline infix fun <reified T> NamedValue<ClosedRange<T>>.between(range: ClosedRange<T>)
//                where T : Number, T : Comparable<T> {
//
//            when (this.name) {
//                "starRatingRange" -> {
//                    if (range.start.toDouble() < 0.0)
//                        throw IllegalArgumentException("Range is Between 0.0 - 5.0, inclusive, you put in '${range.start}' which is less than 0.0")
//                    if (range.endInclusive.toDouble() > 5.0)
//                        throw IllegalArgumentException("Range is Between 0.0 - 5.0, inclusive, you put in '${range.endInclusive}' which is higher than 5.0")
//                    if (range.start.toDouble() > range.endInclusive.toDouble())
//                        throw IllegalArgumentException("fromRange range cannot be higher than toRange")
//
//                    this@HotelSearchBuilder.rating.value = range as ClosedRange<Double>
//                }
//
//                "priceRange" -> {
//                    this@HotelSearchBuilder.price.value = range as ClosedRange<Int>
//                }
//
//                else -> {
//                    throw IllegalArgumentException("Invalid Variable: ${this.name}")
//                }
//            }
//        }
//
//
//    }
//
//    data class HotelQuery(
//        var id: Int? = null,
//
//        val name: String? = null,
//        val city: String? = null,
//        val address: String? = null,
//
//        val fromRating: Double? = null,
//        val toRating: Double? = null,
//
//        val fromPrice: Int? = null,
//        val toPrice: Int? = null,
//
//        val fromDate: LocalDate? = null,
//        val toDate: LocalDate? = null,
//
//        val amenities: MutableSet<Amenity>? = null,
//    )
//}
//
//fun main() {
//    val h = HotelService.queryBuilder {
//
//        rating between (2.0..3.0)
//        price between (400..2000)
//
//    }
//}




