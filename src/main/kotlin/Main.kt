package ice.private.brynj


import ice.private.brynj.database.DatabaseManager
import uiclasses.HomeApplication
import javafx.application.Application


fun main() {
    DatabaseManager.init()
    
    Application.launch(HomeApplication::class.java)

}