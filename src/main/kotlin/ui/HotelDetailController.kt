package ui

import javafx.fxml.FXML
import javafx.scene.control.Label
import model.Hotel

class HotelDetailController {

    @FXML
    private lateinit var hotelNameLabel: Label
    @FXML
    private lateinit var hotelCityLabel: Label
    @FXML
    private lateinit var hotelRatingLabel: Label
    @FXML
    private lateinit var hotelDescriptionLabel: Label

    fun setHotel(hotel: Hotel) {
        hotelNameLabel.text = hotel.name
        hotelCityLabel.text = "City: ${hotel.city}"
        hotelRatingLabel.text = "Rating: ${hotel.starRating}"
        hotelDescriptionLabel.text = hotel.description ?: "No description"
    }
}