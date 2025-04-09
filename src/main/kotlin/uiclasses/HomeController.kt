package uiclasses

import ice.private.brynj.model.Amenity
import ice.private.brynj.model.Hotel
import ice.private.brynj.service.HotelService
import ice.private.brynj.utils.SearchCriteria
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.text.Text
import javafx.util.Callback
import javafx.event.ActionEvent
import java.net.URL
import java.util.*
import javafx.scene.control.CheckBox
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class HomeController : Initializable {

    @FXML
    private lateinit var cityTextField: TextField

    @FXML
    private lateinit var listView: ListView<Hotel> // Use Hotel as the type

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

    private val hotelService = HotelService() // Create an instance of HotelService

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
                // Optionally, set the default state
                selectedProperty().set(false)
            }
        })
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        // Load all hotels initially
        loadAllHotels()

        val checkBoxes = createAmenityCheckBoxes()
        amenitiesListView.items = checkBoxes

        // Set custom cell factory to display only hotel names
        listView.cellFactory = Callback { param ->
            object : javafx.scene.control.ListCell<Hotel>() {
                override fun updateItem(item: Hotel?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (empty || item == null) {
                        "" // Display nothing if empty
                    } else {
                        item.name // Display only the hotel's name
                    }

                    // Set hover event to display hotel details in the right box
                    setOnMouseEntered {
                        if (item != null) {
                            showHotelDetails(item)
                        }
                    }

                    setOnMouseExited {
                        // Clear hotel details when mouse exits the list item
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
                val fxmlFileName = "/UI/hotelView.fxml" // Replace with the actual FXML file path
                val sceneSwitcher = SceneSwitcher()
                sceneSwitcher.switchSceneHotel(event, fxmlFileName, selectedHotel)
            }
        }
    }

    private fun loadAllHotels() {
        // Fetch all hotels and populate the ListView
        val hotels = hotelService.searchHotels(SearchCriteria()) // Empty criteria to get all hotels
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
            .filter { it.selectedProperty().get() }  // Filter selected checkboxes
            .map { Amenity(it.text) }  // Assuming Amenity has a constructor taking the name

        // Create SearchCriteria
        val criteria = SearchCriteria(
            city = city.takeIf { it.isNotBlank() },
            minPricePerNight = minPrice,
            maxPricePerNight = maxPrice,
            minStarRating = minStarRating,
            maxStarRating = maxStarRating,
            selectedAmenities = selectedAmenities.toSet()  // Convert to Set<Amenity>
        )

        // Perform the search and update the ListView
        val hotels = hotelService.searchHotels(criteria)
        listView.items.setAll(hotels)
    }


    private fun showHotelDetails(hotel: Hotel) {
        // Display the details of the hovered hotel in the right section
        hotelName.text = "Name: ${hotel.name}"
        hotelAddress.text = "Address: ${hotel.address}"
        hotelCity.text = "City: ${hotel.city}"
        hotelStarRating.text = "Star Rating: ${hotel.starRating}"
        hotelDescription.text = "Description: ${hotel.description ?: "No description available"}"
    }

    private fun clearHotelDetails() {
        // Clear the details from the right section when the hover is removed
        hotelName.text = ""
        hotelAddress.text = ""
        hotelCity.text = ""
        hotelStarRating.text = ""
        hotelDescription.text = ""
    }
}
