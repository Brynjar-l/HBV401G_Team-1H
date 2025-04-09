package model

import java.time.LocalDate


data class Booking(

    val room: Room,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val totalPrice: Int,

    val id: Int? = null,

    )
