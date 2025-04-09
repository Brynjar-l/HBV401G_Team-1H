package ui

import model.Hotel
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.control.Button
import javafx.scene.control.Alert
import javafx.fxml.Initializable
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.net.URL
import java.util.*
import javafx.scene.text.Text


class HotelDetailsController : Initializable {

    @FXML private lateinit var hotelName: Text
    @FXML private lateinit var hotelAddress: Text
    @FXML private lateinit var hotelCity: Text
    @FXML private lateinit var hotelStarRating: Text
    @FXML private lateinit var hotelDescription: Text
    @FXML private lateinit var roomListView: ListView<String>  
    @FXML private lateinit var bookingButton: Button  

    private lateinit var hotel: Hotel

    // This method is used to pass the Hotel object to the controller
    fun setHotel(hotel: Hotel) {
        this.hotel = hotel
        hotelName.text = hotel.name
        hotelAddress.text = hotel.address
        hotelCity.text = hotel.city
        hotelStarRating.text = "Star Rating: ${hotel.starRating}"
        hotelDescription.text = hotel.description ?: "No description available."


        displayRooms()
    }

    // This method displays the list of rooms in the hotel using ListView
    private fun displayRooms() {
        val roomDescriptions: ObservableList<String> = FXCollections.observableArrayList()

        hotel.rooms?.forEach { room ->
            val roomDescription = "Room Number: ${room.roomNumber}, Price per Night: ${room.pricePerNight}, Beds: ${room.numberOfBeds}"
            roomDescriptions.add(roomDescription)
        }


        roomListView.items = roomDescriptions
    }

    // This method is automatically called by FXMLLoader when the scene is loaded
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bookingButton.setOnAction { 
            onBookingButtonClick()
        }

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

    // Method to handle booking button click
    private fun onBookingButtonClick() {
        // Get the selected room from the ListView
        val selectedRoomDescription = roomListView.selectionModel.selectedItem

        if (selectedRoomDescription != null) {
            val selectedRoom = hotel.rooms?.firstOrNull { room ->
                selectedRoomDescription.contains("Room Number: ${room.roomNumber}")
            }



            /*
            the whole booking face
             */

            if (selectedRoom != null) {
                // Perform the booking action (example: show a confirmation alert)
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Booking Confirmation"
                alert.headerText = "Room Booked Successfully!"
                alert.contentText = "You have successfully booked Room ${selectedRoom.roomNumber} for ${selectedRoom.pricePerNight} per night."
                alert.showAndWait()
            } else {
                // Handle case when no room is selected
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Error"
                alert.headerText = "No Room Selected"
                alert.contentText = "Please select a room before booking."
                alert.showAndWait()
            }
        } else {
            // Handle case when no room is selected
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Error"
            alert.headerText = "No Room Selected"
            alert.contentText = "Please select a room before booking."
            alert.showAndWait()
        }
    }
}
