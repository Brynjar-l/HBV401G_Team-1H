import database.DatabaseManager
import javafx.application.Application
import ui.HotelSearchApp
import ui.MainViewController


fun main() {
    DatabaseManager.init()
    Application.launch(HotelSearchApp::class.java)
}