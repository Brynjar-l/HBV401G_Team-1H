package app


import database.DatabaseManager
import javafx.application.Application


fun main() {
    DatabaseManager.init()
    
    Application.launch(HotelSearchApplication::class.java)

}