package ui

import model.Amenity
import model.Hotel
import service.HotelService
import ui.nav.SceneSwitcher
import utils.SearchCriteria
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.text.Text
import javafx.util.Callback
import java.net.URL
import java.util.*
import javafx.scene.control.CheckBox
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class HotelSearchController : Initializable {

    @FXML
    private lateinit var cityTextField: TextField

    @FXML
    private lateinit var listView: ListView<Hotel>

    @FXML
    private lateinit var maxPriceTextField: TextField

    @FXML
    private lateinit var minPriceTextField: TextField

    @FXML
    private lateinit var searchButton: Button

    @FXML
    private lateinit var hotelName: Text

    @FXML
    private lateinit var hotelAddress: Text

    @FXML
    private lateinit var hotelCity: Text

    @FXML
    private lateinit var hotelStarRating: Text

    @FXML
    private lateinit var hotelDescription: Text

    @FXML
    private lateinit var minStarRatingTextField: TextField

    @FXML
    private lateinit var maxStarRatingTextField: TextField

    private val hotelService = HotelService()
    @FXML
    private lateinit var amenitiesListView: ListView<CheckBox>

    fun createAmenityCheckBoxes(): ObservableList<CheckBox> {
        val amenities = listOf(
            "Free Wi-Fi",
            "Parking",
            "24-Hour Front Desk",
            "Fitness Center",
            "Swimming Pool",
            "Spa & Wellness Center",
            "Airport Shuttle",
            "Room Service",
            "Restaurant",
            "Bar",
            "Laundry Service",
            "Non-Smoking Rooms",
            "Pet-Friendly",
            "Business Center",
            "Air Conditioning",
            "Heating",
            "Flat-Screen TV",
            "Mini-Bar",
            "Safe Deposit Box",
            "Concierge Service"
        )

        return FXCollections.observableArrayList(amenities.map { amenity ->
            CheckBox(amenity).apply {
                selectedProperty().set(false)
            }
        })
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        // Load all hotels initially to fill the list for the user
        loadAllHotels()

        val checkBoxes = createAmenityCheckBoxes()
        amenitiesListView.items = checkBoxes

        // Set custom cell factory to show hotel names only
        listView.cellFactory = Callback { param ->
            object : javafx.scene.control.ListCell<Hotel>() {
                override fun updateItem(item: Hotel?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (empty || item == null) {
                        "" // Display nothing if empty
                    } else {
                        item.name 
                    }

                    //to show detail if hovered over hotel
                    setOnMouseEntered {
                        if (item != null) {
                            showHotelDetails(item)
                        }
                    }

                    setOnMouseExited {
                        // Clear hotel details when mouse is no longer hovering over the hotel
                        clearHotelDetails()
                    }
                }
            }
        }

        // Set click event handler on ListView
        listView.setOnMouseClicked { event ->
            val selectedHotel = listView.selectionModel.selectedItem
            if (selectedHotel != null) {
                // Use SceneSwitcher to switch to the hotel details scene
                val fxmlFileName = "/UI/hotelDetailsView.fxml"
                val sceneSwitcher = SceneSwitcher()
                sceneSwitcher.switchSceneHotel(event, fxmlFileName, selectedHotel)
            }
        }
    }

    // Fetch all hotels and populate the ListView
    private fun loadAllHotels() {
        val hotels = hotelService.searchHotels(SearchCriteria())
        listView.items.setAll(hotels)
    }

    @FXML
    fun onSearchButtonClick() {
        val city = cityTextField.text
        val minPrice = minPriceTextField.text.toIntOrNull()
        val maxPrice = maxPriceTextField.text.toIntOrNull()
        val minStarRating = minStarRatingTextField.text.toDoubleOrNull()
        val maxStarRating = maxStarRatingTextField.text.toDoubleOrNull()

        // Collect selected amenities
        val selectedAmenities = amenitiesListView.items
            .filter { it.selectedProperty().get() }  
            .map { Amenity(it.text) }

        // Create SearchCriteria
        val criteria = SearchCriteria(
            city = city.takeIf { it.isNotBlank() },
            minPricePerNight = minPrice,
            maxPricePerNight = maxPrice,
            minStarRating = minStarRating,
            maxStarRating = maxStarRating,
            selectedAmenities = selectedAmenities.toSet()  
        )

        // Perform the search and update the ListView
        val hotels = hotelService.searchHotels(criteria)
        listView.items.setAll(hotels)
    }

    // Display the details of the hovered hotel in the right section
    private fun showHotelDetails(hotel: Hotel) {
        hotelName.text = "Name: ${hotel.name}"
        hotelAddress.text = "Address: ${hotel.address}"
        hotelCity.text = "City: ${hotel.city}"
        hotelStarRating.text = "Star Rating: ${hotel.starRating}"
        hotelDescription.text = "Description: ${hotel.description ?: "No description available"}"
    }

    //make details empty when removed from hover
    private fun clearHotelDetails() {
        hotelName.text = ""
        hotelAddress.text = ""
        hotelCity.text = ""
        hotelStarRating.text = ""
        hotelDescription.text = ""
    }
}
