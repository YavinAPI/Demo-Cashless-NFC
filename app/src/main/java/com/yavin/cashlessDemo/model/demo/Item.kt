package com.yavin.cashlessDemo.model.demo

data class Item(val name: String, val totalPriceInCents: Int, val quantity: Int)

val dummyItems = listOf(
    Item("Bière", 1500, quantity = 3),
    Item("Coca cola", 350, quantity = 1),
    Item("Planche à partager", 2000, quantity = 1),
)

