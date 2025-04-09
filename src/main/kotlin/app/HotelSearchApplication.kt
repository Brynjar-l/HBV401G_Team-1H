package app

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class HotelSearchApplication : Application() {

    override fun start(stage: Stage) {
        try {
            val loader = FXMLLoader(javaClass.getResource("/UI/hotelSearchView.fxml"))
            val root: Parent = loader.load()
            val scene = Scene(root)
            stage.scene = scene
            stage.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Handle window close event
        stage.setOnCloseRequest { 
            Platform.exit()
            System.exit(0)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(HotelSearchApplication::class.java)
        }
    }
}
