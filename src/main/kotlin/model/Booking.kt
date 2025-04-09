package model

import java.time.LocalDate


data class Booking(

    val roomId: Int,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val totalPrice: Int,

    val id: Int? = null,

    )
