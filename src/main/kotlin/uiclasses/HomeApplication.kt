package uiclasses

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.WindowEvent

class HomeApplication : Application() {

    override fun start(stage: Stage) {
        try {
            val loader = FXMLLoader(javaClass.getResource("/UI/homeView.fxml"))
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
            launch(HomeApplication::class.java)
        }
    }
}
