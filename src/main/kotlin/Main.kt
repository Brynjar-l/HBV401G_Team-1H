package ice.private.brynj

import ice.private.brynj.database.DatabaseManager
import ice.private.brynj.database.entities.HotelEntity
import org.jetbrains.exposed.sql.transactions.transaction


fun main() {
    DatabaseManager.init()


}