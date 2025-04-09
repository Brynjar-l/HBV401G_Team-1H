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
import service.AmenityService
import service.HotelService
import utils.SearchCriteria


class MainViewController {

    @FXML private lateinit var cityField: TextField
    @FXML private lateinit var minRatingField: TextField
    @FXML private lateinit var maxRatingField: TextField
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

        // Row factory to handle double-click
        hotelTable.setRowFactory { tableView ->
            val row = TableRow<Hotel>()
            row.setOnMouseClicked { event ->
                if (!row.isEmpty && event.clickCount == 2) {
                    showHotelDetail(row.item)
                }
            }
            row
        }

        // Load all amenities
        val allAmenities = amenityService.loadAllAmenities()
        for (amenity in allAmenities) {
            val checkBox = CheckBox(amenity.name)
            checkBox.selectedProperty().addListener { _, _, _ -> doSearch() }
            amenitiesBox.children.add(checkBox)
            amenityCheckboxes[amenity] = checkBox
        }

        // Press 'Enter' in these fields => doSearch
        cityField.setOnAction { doSearch() }
        minRatingField.setOnAction { doSearch() }
        maxRatingField.setOnAction { doSearch() }
        fromDatePicker.setOnAction { doSearch() }
        toDatePicker.setOnAction { doSearch() }

        doSearch() // initial load
    }

    @FXML
    fun onSearchClicked() = doSearch()

    @FXML
    fun onClearClicked() {
        cityField.clear()
        minRatingField.clear()
        maxRatingField.clear()
        fromDatePicker.value = null
        toDatePicker.value = null
        amenityCheckboxes.values.forEach { it.isSelected = false }
        doSearch()
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
        val loader = FXMLLoader(javaClass.getResource("/ui/hotelDetailView.fxml"))
        val root = loader.load<BorderPane>()
        val controller = loader.getController<HotelDetailController>()
        controller.setHotel(hotel)

        val stage = Stage()
        stage.title = "Hotel Details"
        stage.scene = Scene(root)
        stage.show()
    }
}
