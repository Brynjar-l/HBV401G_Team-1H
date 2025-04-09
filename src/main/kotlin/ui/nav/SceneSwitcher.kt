package ui.nav

import javafx.event.Event
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import model.Hotel
import ui.HotelDetailsController

class SceneSwitcher {

    fun switchSceneHotel(event: Event, fxmlFileName: String, selectedHotel: Hotel) {
        // Load the FXML file for the hotel details scene
        val loader = FXMLLoader(javaClass.getResource(fxmlFileName))
        
        // Force the result of load to be cast to Parent
        val root: Parent = loader.load() as Parent

        // Get the controller for the loaded FXML and set the hotel
        val hotelDetailsController = loader.getController() as HotelDetailsController

        // Set the hotel in the controller
        hotelDetailsController.setHotel(selectedHotel)

        // Get the current stage and switch the scene
        val stage = (event.source as Node).scene.window as Stage
        val scene = Scene(root)
        stage.scene = scene
        stage.show()
    }
}
