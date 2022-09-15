package com.dicoding.kedaikopi.ui.screen.cart

import com.dicoding.kedaikopi.model.OrderCoffee

data class CartState(
    val orderCoffeeDrinks: List<OrderCoffee>,
    val totalPrice: Int
)