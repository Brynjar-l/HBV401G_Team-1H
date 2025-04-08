package uiclasses

import ice.private.brynj.model.Hotel
import javafx.fxml.FXML
import javafx.scene.text.Text
import javafx.fxml.Initializable
import java.net.URL
import java.util.*

class HotelController : Initializable {

    @FXML private lateinit var hotelName: Text
    @FXML private lateinit var hotelAddress: Text
    @FXML private lateinit var hotelCity: Text
    @FXML private lateinit var hotelStarRating: Text
    @FXML private lateinit var hotelDescription: Text

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
        } else {
            println("Hotel object is not initialized.")
        }
    }
}
