package ui

import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import model.Hotel
import model.Room
import service.BookingService
import java.time.LocalDate

class HotelDetailController {

    @FXML
    private lateinit var hotelNameLabel: Label

    @FXML
    private lateinit var roomsTable: TableView<Room>
    @FXML
    private lateinit var roomNumberColumn: TableColumn<Room, String>
    @FXML
    private lateinit var roomPriceColumn: TableColumn<Room, String>
    @FXML
    private lateinit var roomBedsColumn: TableColumn<Room, String>

    @FXML
    private lateinit var statusLabel: Label

    private val bookingService = BookingService()
    private lateinit var currentHotel: Hotel

    fun setHotel(hotel: Hotel) {
        currentHotel = hotel
        hotelNameLabel.text = hotel.name
        loadRooms(hotel.rooms)
    }

    @FXML
    fun initialize() {
        roomNumberColumn.setCellValueFactory {
            SimpleStringProperty(it.value.roomNumber ?: "")
        }
        roomPriceColumn.setCellValueFactory {
            SimpleStringProperty(it.value.pricePerNight.toString())
        }
        roomBedsColumn.setCellValueFactory {
            SimpleStringProperty(it.value.numberOfBeds.toString())
        }

        roomsTable.setRowFactory { _ ->
            val row = TableRow<Room>()
            row.setOnMouseClicked { event ->
                if (!row.isEmpty && event.clickCount == 2) {
                    onBookClicked()
                }
            }
            row
        }
    }

    private fun loadRooms(rooms: List<Room>) {
        roomsTable.items = javafx.collections.FXCollections.observableArrayList(rooms)
    }

    @FXML
    fun onBookClicked() {
        val selectedRoom = roomsTable.selectionModel.selectedItem
            ?: return showError("No room selected.")

        val from = LocalDate.now().plusDays(1)
        val to = LocalDate.now().plusDays(3)

        try {
            val newBooking = bookingService.createBooking(
                roomId = selectedRoom.id,
                fromDate = from,
                toDate = to
            )
            showSuccess("Booking created for Room #${selectedRoom.roomNumber} from $from to $to!")
        } catch (ex: IllegalArgumentException) {
            showError("Room is unavailable: ${ex.message}")
        } catch (ex: IllegalStateException) {
            showError("Overlap error: ${ex.message}")
        }
    }

    private fun showError(msg: String) {
        statusLabel.text = msg
        statusLabel.style = "-fx-text-fill: red;"
    }

    private fun showSuccess(msg: String) {
        statusLabel.text = msg
        statusLabel.style = "-fx-text-fill: green;"
    }
}
