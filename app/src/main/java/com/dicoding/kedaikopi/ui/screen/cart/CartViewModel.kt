package com.dicoding.kedaikopi.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kedaikopi.data.CoffeeRepository
import com.dicoding.kedaikopi.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CoffeeRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CartState>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CartState>>
        get() = _uiState

    fun loadCoffeeDrinks() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAddedOrderCoffeeDrinks()
                .collect { orderCoffeeDrinks ->
                    val totalPrice = orderCoffeeDrinks.sumOf { it.coffee.price * it.count }
                    _uiState.value = UiState.Success(CartState(orderCoffeeDrinks, totalPrice))
                }
        }
    }

    fun clear() {
        viewModelScope.launch {
            repository.clear()
        }
    }
}