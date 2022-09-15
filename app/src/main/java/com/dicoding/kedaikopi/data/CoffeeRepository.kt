package com.dicoding.kedaikopi.data

import com.dicoding.kedaikopi.model.Coffee
import com.dicoding.kedaikopi.model.FakeCoffeeDataSource
import com.dicoding.kedaikopi.model.OrderCoffee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class CoffeeRepository {



    private val orderCoffeeDrinks = mutableListOf<OrderCoffee>()

    init {
        if (orderCoffeeDrinks.isEmpty()) {
            FakeCoffeeDataSource.dummyCoffees.map {
                orderCoffeeDrinks.add(OrderCoffee(it, 0))
            }
        }
    }
    suspend fun getAllOrderCoffeeDrinks(): Flow<List<OrderCoffee>> {
//        if (orderCoffeeDrinks.isEmpty()) {
//            FakeCoffeeDataSource.dummyCoffees.map {
//                orderCoffeeDrinks.add(OrderCoffee(it, 0))
//            }
//        }
        return flowOf(orderCoffeeDrinks)
    }

    suspend fun getCoffeeById(coffeeDrinkId: Long): Coffee {
        return FakeCoffeeDataSource.dummyCoffees.first {
            it.id == coffeeDrinkId
        }
    }

    suspend fun getAddedOrderCoffeeDrinks(): Flow<List<OrderCoffee>> {
            return getAllOrderCoffeeDrinks()
            .map { orderCoffeeDrinks ->
                orderCoffeeDrinks.filter { orderCoffeeDrink ->
                    orderCoffeeDrink.count != DEFAULT_COFFEE_DRINK_COUNT
                }
            }
    }

    suspend fun add(coffeeDrinkId: Long, count: Int): Flow<Boolean> {
        val index = orderCoffeeDrinks.indexOfFirst { it.coffee.id == coffeeDrinkId }
        val result = if (index != INVALID_INDEX) {
            val orderCoffeeDrink = orderCoffeeDrinks[index]
            val newCountValue = count
            orderCoffeeDrinks[index] = orderCoffeeDrink.copy(coffee = orderCoffeeDrink.coffee, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun addCoffeeToCart(coffee: Coffee, count: Int) {
        orderCoffeeDrinks.add(OrderCoffee(coffee, count))
    }

    suspend fun remove(coffeeDrinkId: Long): Flow<Boolean> {
        val index = orderCoffeeDrinks.indexOfFirst { it.coffee.id == coffeeDrinkId }
        val result = if (index != INVALID_INDEX) {
            val orderCoffeeDrink = orderCoffeeDrinks[index]
            val newCountValue = if (orderCoffeeDrink.count == MIN_COFFEE_DRINK_VALUE)
                MIN_COFFEE_DRINK_VALUE
            else
                orderCoffeeDrink.count - 1

            orderCoffeeDrinks[index] =
                orderCoffeeDrink.copy(coffee = orderCoffeeDrink.coffee, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    suspend fun clear() {
        orderCoffeeDrinks.clear()
    }

    companion object {
        private const val MIN_COFFEE_DRINK_VALUE = 0
        private const val MAX_COFFEE_DRINK_VALUE = 99
        private const val DEFAULT_COFFEE_DRINK_COUNT = 0
        private const val INVALID_INDEX = -1

        @Volatile
        private var instance: CoffeeRepository? = null

        fun getInstance(): CoffeeRepository =
            instance ?: synchronized(this) {
                CoffeeRepository().apply {
                    instance = this
                }
            }
    }
}