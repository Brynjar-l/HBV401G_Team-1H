package uiclasses

import ice.private.brynj.model.Hotel
import ice.private.brynj.model.Room
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.fxml.Initializable
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.net.URL
import java.util.*

class HotelController : Initializable {

    @FXML private lateinit var hotelName: Text
    @FXML private lateinit var hotelAddress: Text
    @FXML private lateinit var hotelCity: Text
    @FXML private lateinit var hotelStarRating: Text
    @FXML private lateinit var hotelDescription: Text
    @FXML private lateinit var roomListView: ListView<String>  // ListView to display rooms

    private lateinit var hotel: Hotel

    // This method is used to pass the Hotel object to the controller
    fun setHotel(hotel: Hotel) {
        this.hotel = hotel
        // Initialize the scene with hotel data when set
        hotelName.text = hotel.name
        hotelAddress.text = hotel.address
        hotelCity.text = hotel.city
        hotelStarRating.text = "Star Rating: ${hotel.starRating}"
        hotelDescription.text = hotel.description ?: "No description available."

        // Now display rooms
        displayRooms()
    }

    // This method displays the list of rooms in the hotel using ListView
    private fun displayRooms() {
        val roomDescriptions: ObservableList<String> = FXCollections.observableArrayList()

        // Iterate through the rooms and add their descriptions to the ListView
        hotel.rooms?.forEach { room ->
            val roomDescription = "Room Number: ${room.roomNumber}, Price per Night: ${room.pricePerNight}, Beds: ${room.numberOfBeds}"
            roomDescriptions.add(roomDescription)
        }

        // Set the ListView's items to the roomDescriptions list
        roomListView.items = roomDescriptions
    }

    // This method is automatically called by FXMLLoader when the scene is loaded
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        // Only set the text if the hotel is already initialized
        if (::hotel.isInitialized) {
            hotelName.text = hotel.name
            hotelAddress.text = hotel.address
            hotelCity.text = hotel.city
            hotelStarRating.text = "Star Rating: ${hotel.starRating}"
            hotelDescription.text = hotel.description ?: "No description available."
            displayRooms()
        } else {
            println("Hotel object is not initialized.")
        }
    }
}
