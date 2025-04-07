package ice.private.brynj

import com.varabyte.kotter.foundation.collections.liveListOf
import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.black
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.runtime.Session
import ice.private.brynj.database.DatabaseManager
import ice.private.brynj.model.Amenity
import ice.private.brynj.service.HotelService
import java.util.*

fun Enum<*>.toDisplayName(capitalized: Boolean = false): String {
    return name.split("_").joinToString(" ") { s ->
        s.lowercase()
            .let { w -> if (capitalized) w.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } else w }
    }
}

enum class Filters {
    LOCATION,
    AMENITIES,
    RATING,
    PRICE,
    DATE,
}

fun Session.selectAmenities() {}
fun Session.selectRating() {}
fun Session.selectLocation() {}
fun Session.selectPrice() {}
fun Session.selectDate() {}

fun Session.mainScreen() {}

fun main() {
    DatabaseManager.init()
    val hs = HotelService()


    session {
        var cursorIndex by liveVarOf(0)

        var selectedLocation by liveVarOf<String?>(null)

        var selectedFromDate by liveVarOf<String?>(null)
        var selectedToDate by liveVarOf<String?>(null)

        var selectedPriceLowerBound by liveVarOf<Double?>(null)
        var selectedPriceUpperBound by liveVarOf<Double?>(null)

        var selectedRatingLowerBound by liveVarOf<Double?>(null)
        var selectedRatingUpperBound by liveVarOf<Double?>(null)

        val selectedAmenities = liveListOf<Amenity>()

        section {
            Filters.entries.forEachIndexed { i, dish ->
                text(if (i == cursorIndex) ">" else "${i + 1}."); text(' ')
                textLine(dish.toDisplayName(capitalized = true))
            }
            textLine()
            black(isBright = true) { textLine("Use UP/DOWN to choose and ENTER to select") }
            textLine()
        }.runUntilSignal {
            onKeyPressed {
                when (key) {
                    Keys.UP -> cursorIndex -= 1
                    Keys.DOWN -> cursorIndex += 1

                    Keys.DIGIT_1 -> cursorIndex = 0
                    Keys.DIGIT_2 -> cursorIndex = 1
                    Keys.DIGIT_3 -> cursorIndex = 2
                    Keys.DIGIT_4 -> cursorIndex = 3
                    Keys.DIGIT_5 -> cursorIndex = 4

                    Keys.ENTER -> {
                        println("${Filters.entries[cursorIndex]} selected")
                    }

                    Keys.ESC -> signal()
                }

                if (cursorIndex < 0) cursorIndex = Filters.entries.toTypedArray().lastIndex
                else if (cursorIndex > Filters.entries.toTypedArray().lastIndex) cursorIndex = 0
            }
        }
    }
}

