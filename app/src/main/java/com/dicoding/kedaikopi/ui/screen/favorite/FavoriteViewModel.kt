package com.dicoding.kedaikopi.ui.screen.favorite

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kedaikopi.data.CoffeeRepository
import com.dicoding.kedaikopi.model.OrderCoffee
import com.dicoding.kedaikopi.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: CoffeeRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<OrderCoffee>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<OrderCoffee>>>
        get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun loadCoffeeDrinks() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAllOrderCoffeeDrinks()
                .collect { orderCoffeeDrinks ->
                    _uiState.value = UiState.Success(orderCoffeeDrinks)
                }
        }
    }

    fun search(newQuery: String) {
        _query.value = newQuery
        _uiState.value = UiState.Success(repository.searchCoffee(_query.value))
    }

    fun clear() {
        viewModelScope.launch {
            repository.clear()
        }
    }

}