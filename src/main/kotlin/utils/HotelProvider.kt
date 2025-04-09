package utils

import database.entities.HotelEntity
import model.Hotel
import org.jetbrains.exposed.sql.transactions.transaction

typealias HotelProvider = () -> List<Hotel>

fun defaultProvider(): HotelProvider = {
    transaction {
        HotelEntity.all().map { it.toDto() }
    }
}