package service

import model.Amenity
import model.Hotel
import model.Room
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import utils.HotelProvider
import utils.SearchCriteria
import java.time.LocalDate
import kotlin.test.*

class HotelServiceTest {

    private val wifiAmenity = Amenity("WiFi", 1)
    private val breakfastAmenity = Amenity("Breakfast", 2)
    private val parkingAmenity = Amenity("Parking", 3)

    private val hotelA_Room1 = Room("101", 2000, 2, 1, 1, emptyList())
    private val hotelA_Room2 = Room("102", 6000, 3, 2, 1, listOf(LocalDate.of(2025, 5, 1) to LocalDate.of(2025, 5, 3)))
    private val hotelB_Room1 = Room("201", 4500, 1, 3, 2, listOf(LocalDate.of(2025, 4, 30) to LocalDate.of(2025, 5, 2)))
    private val hotelB_Room2 = Room("202", 5000, 2, 4, 2, emptyList())
    private val hotelC_Room1 = Room("301", 8000, 4, 5, 3, emptyList())

    private val hotelA = Hotel( "Alpha Hotel", "addressA", "Paris", 3.5, null, 1, listOf(hotelA_Room1, hotelA_Room2), listOf(wifiAmenity, parkingAmenity))
    private val hotelB = Hotel( "Bravo Hotel", "addressB", "Rome", 4.0, null, 2, listOf(hotelB_Room1, hotelB_Room2), listOf(breakfastAmenity))
    private val hotelC = Hotel( "Charlie Resort", "addressC", "Paris", 5.0, null, 3, listOf(hotelC_Room1), listOf(wifiAmenity, breakfastAmenity))


    private val testHotels = listOf(hotelA, hotelB, hotelC)
    private val testProvider: HotelProvider = { testHotels }

    private lateinit var service: HotelService


    @BeforeEach
    fun setup() {
        service = HotelService(testProvider)
    }


    @Test
    fun callingSearchHotels_onMockData_hitsServiceLogic() {
        val results = service.searchHotels(SearchCriteria())
        assertEquals(3, results.size)
    }


    @Test
    @DisplayName("test no criteria returns all hotels")
    fun noCriteria_returnsAllHotels() {
        val results = service.searchHotels(SearchCriteria())
        assertEquals(3, results.size)
    }

    @Test
    @DisplayName("test city filter - Paris")
    fun cityFilter_paris_returns2Hotels() {
        val results = service.searchHotels(SearchCriteria(city = "Paris"))
        assertEquals(2, results.size)
    }

    @Test
    @DisplayName("test amenity filter - WiFi")
    fun amenityFilter_wifi_returns2Hotels() {
        val criteria = SearchCriteria(selectedAmenities = setOf(wifiAmenity))
        val results = service.searchHotels(criteria)
        assertEquals(2, results.size)
    }

    @Test
    @DisplayName("test star rating filter - min 4, max 5")
    fun starRatingFilter_min4Max5_returnsHotels2And3() {
        val criteria = SearchCriteria(minStarRating = 4.0, maxStarRating = 5.0)
        val results = service.searchHotels(criteria)
        assertEquals(2, results.size)
    }

    @Test
    @DisplayName("test price filter - 4000 to 6000")
    fun priceFilter_4000to6000_excludesHotelC() {
        val criteria = SearchCriteria(minPricePerNight = 4000, maxPricePerNight = 6000)
        val results = service.searchHotels(criteria)
        assertEquals(2, results.size)
    }

    @Test
    @DisplayName("test date availability filter")
    fun dateFilter_2025_05_01to2025_05_03_includesAllButTrimRooms() {
        val criteria = SearchCriteria(
            fromDate = LocalDate.of(2025, 5, 1),
            toDate = LocalDate.of(2025, 5, 3)
        )
        val results = service.searchHotels(criteria)
        assertEquals(3, results.size)
    }

    @Test
    @DisplayName("test number of beds filter")
    fun bedsFilter_atLeast2_returnsAllHotelsButTrimSomeRooms() {
        val criteria = SearchCriteria(numberOfBeds = 2)
        val results = service.searchHotels(criteria)
        assertEquals(3, results.size)
    }

    @Test
    @DisplayName("test combined filters - city, star rating, price, and date")
    fun combinedFilters_paris4Star4000to6000_2025_05_01to2025_05_03_returnsEmpty() {
        val criteria = SearchCriteria(
            city = "Paris",
            minStarRating = 4.0,
            minPricePerNight = 4000,
            maxPricePerNight = 6000,
            fromDate = LocalDate.of(2025, 5, 1),
            toDate = LocalDate.of(2025, 5, 3)
        )
        val results = service.searchHotels(criteria)
        assertTrue(results.isEmpty())
    }

    @Test
    @DisplayName("test empty results still handles gracefully")
    fun cityFilter_tokyo_returnsEmpty() {
        val criteria = SearchCriteria(city = "Tokyo")
        val results = service.searchHotels(criteria)
        assertTrue(results.isEmpty())
    }
}
