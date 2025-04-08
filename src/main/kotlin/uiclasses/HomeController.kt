package uiclasses

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

    private val hotelService = HotelService() // Create an instance of HotelService

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        // Load all hotels initially
        loadAllHotels()

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
        // Get values from the text fields
        val city = cityTextField.text
        val minPrice = minPriceTextField.text.toIntOrNull() // Parse as Int if valid
        val maxPrice = maxPriceTextField.text.toIntOrNull()

        // Create SearchCriteria based on the input values
        val criteria = SearchCriteria(
            city = city.takeIf { it.isNotBlank() },
            minPricePerNight = minPrice,
            maxPricePerNight = maxPrice
        )

        // Search for hotels based on the criteria
        val hotels = hotelService.searchHotels(criteria)

        // Update the ListView with the search results
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
