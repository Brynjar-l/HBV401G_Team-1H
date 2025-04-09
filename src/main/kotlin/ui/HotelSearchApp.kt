package ui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HotelSearchApp : Application() {
    override fun start(primaryStage: Stage) {
        val loader = FXMLLoader(javaClass.getResource("/ui/mainView.fxml"))
        val root = loader.load<javafx.scene.Parent>()
        val scene = Scene(root)
        primaryStage.scene = scene
        primaryStage.title = "Hotel Search"
        primaryStage.show()
    }
}