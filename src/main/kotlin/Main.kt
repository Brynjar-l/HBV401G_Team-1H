import database.DatabaseManager
import javafx.application.Application
import ui.HotelSearchApp


fun main() {
    DatabaseManager.init()
    Application.launch(HotelSearchApp::class.java)
}