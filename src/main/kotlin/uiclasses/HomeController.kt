package uiclasses

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField

class HomeController {

    @FXML
    private lateinit var cityTextField: TextField

    @FXML
    private lateinit var listView: ListView<Any>

    @FXML
    private lateinit var maxPriceTextField: TextField

    @FXML
    private lateinit var minPriceTextField: TextField

    @FXML
    private lateinit var searchButton: Button

    fun someMethod() {
        // Implementation here
    }

}
