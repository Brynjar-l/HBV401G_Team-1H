package ui

import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import model.Amenity
import model.Hotel
import model.Room
import service.AmenityService
import service.HotelService
import utils.SearchCriteria
import java.time.LocalDate


class MainViewController {

    @FXML private lateinit var cityField: TextField

    @FXML private lateinit var minRatingField: TextField
    @FXML private lateinit var maxRatingField: TextField

    @FXML private lateinit var minPriceField: TextField
    @FXML private lateinit var maxPriceField: TextField


    @FXML private lateinit var fromDatePicker: DatePicker
    @FXML private lateinit var toDatePicker: DatePicker

    @FXML private lateinit var amenitiesBox: VBox
    @FXML private lateinit var hotelTable: TableView<Hotel>
    @FXML private lateinit var hotelNameColumn: TableColumn<Hotel, String>
    @FXML private lateinit var hotelCityColumn: TableColumn<Hotel, String>
    @FXML private lateinit var hotelRatingColumn: TableColumn<Hotel, String>

    private val amenityCheckboxes = mutableMapOf<Amenity, CheckBox>()
    private val amenityService = AmenityService()
    private val hotelService = HotelService()

    @FXML
    fun initialize() {
        hotelNameColumn.setCellValueFactory { SimpleStringProperty(it.value.name) }
        hotelCityColumn.setCellValueFactory { SimpleStringProperty(it.value.city) }
        hotelRatingColumn.setCellValueFactory { SimpleStringProperty(it.value.starRating.toString()) }

        hotelTable.setRowFactory { tableView ->
            val row = TableRow<Hotel>()
            row.setOnMouseClicked { event ->
                if (!row.isEmpty && event.clickCount == 2) {
                    showHotelDetail(row.item)
                }
            }
            row
        }

        val allAmenities = amenityService.loadAllAmenities()
        for (amenity in allAmenities) {
            val checkBox = CheckBox(amenity.name)
            checkBox.selectedProperty().addListener { _, _, _ -> doSearch() }
            amenitiesBox.children.add(checkBox)
            amenityCheckboxes[amenity] = checkBox
        }

        cityField.setOnAction { doSearch() }
        minRatingField.setOnAction { doSearch() }
        maxRatingField.setOnAction { doSearch() }
        fromDatePicker.setOnAction { doSearch() }
        toDatePicker.setOnAction { doSearch() }

        doSearch()
    }

    @FXML
    fun onSearchClicked() {
        // check mandatory from/to
        val from = fromDatePicker.value
        val to = toDatePicker.value
        if (from == null || to == null) {
            showAlert("Please select both From and To dates.")
            return
        }
        if (!to.isAfter(from)) {
            showAlert("To date must be after From date.")
            return
        }

        // parse optional min/max rating
        val minRating = minRatingField.text.toDoubleOrNull()
        val maxRating = maxRatingField.text.toDoubleOrNull()

        // parse optional min/max price
        val minPrice = minPriceField.text.toIntOrNull()
        val maxPrice = maxPriceField.text.toIntOrNull()

        val criteria = SearchCriteria(
            city = cityField.text.takeIf { it.isNotBlank() },
            minStarRating = minRating,
            maxStarRating = maxRating,
            minPricePerNight = minPrice,
            maxPricePerNight = maxPrice,
            fromDate = from,
            toDate = to
        )
        val results = hotelService.searchHotels(criteria)
        hotelTable.items = FXCollections.observableArrayList(results)

        if (results.isEmpty()) {
            showAlert("No hotels found for the given criteria.")
        }
    }

    @FXML
    fun onClearClicked() {
        cityField.clear()
        minRatingField.clear()
        maxRatingField.clear()
        minPriceField.clear()
        maxPriceField.clear()
        fromDatePicker.value = null
        toDatePicker.value = null
        hotelTable.items.clear()
    }

    private fun showAlert(msg: String) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Info"
        alert.headerText = null
        alert.contentText = msg
        alert.showAndWait()
    }

    private fun doSearch() {
        val criteria = buildCriteria()
        val results = hotelService.searchHotels(criteria)
        hotelTable.items = FXCollections.observableArrayList(results)
    }

    private fun buildCriteria(): SearchCriteria {
        val selectedAmenities = amenityCheckboxes.filterValues { it.isSelected }.keys
        val minRating = minRatingField.text.toDoubleOrNull()
        val maxRating = maxRatingField.text.toDoubleOrNull()
        val city = cityField.text.takeIf { it.isNotBlank() }
        val from = fromDatePicker.value
        val to = toDatePicker.value

        return SearchCriteria(
            selectedAmenities = selectedAmenities,
            city = city,
            minStarRating = minRating,
            maxStarRating = maxRating,
            fromDate = from,
            toDate = to
        )
    }



    private fun showHotelDetail(hotel: Hotel) {

        val minPrice = minPriceField.text.toIntOrNull() ?: Int.MIN_VALUE
        val maxPrice = maxPriceField.text.toIntOrNull() ?: Int.MAX_VALUE


        val from = fromDatePicker.value
        val to = toDatePicker.value

        val filteredRooms = hotel.rooms.filter { room ->
            val withinPrice = room.pricePerNight in minPrice..maxPrice
            val availableByDate = if (from != null && to != null) {
                isRoomAvailable(room, from, to)
            } else {
                true
            }
            withinPrice && availableByDate
        }


        val filteredHotel = hotel.copy(rooms = filteredRooms)


        val loader = FXMLLoader(javaClass.getResource("/ui/hotelDetailView.fxml"))
        val root = loader.load<BorderPane>()
        val controller = loader.getController<HotelDetailController>()
        controller.setHotel(filteredHotel)

        val stage = Stage()
        stage.title = "Hotel Details"
        stage.scene = Scene(root)
        stage.show()
    }


    private fun isRoomAvailable(room: Room, from: LocalDate, to: LocalDate): Boolean {
        val anyOverlap = room.bookedDates.any { (bookedFrom, bookedTo) ->
            bookedFrom <= to && bookedTo >= from
        }

        return !anyOverlap
    }
}
