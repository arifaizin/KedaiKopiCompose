package com.dicoding.kedaikopi.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kedaikopi.data.CoffeeRepository
import com.dicoding.kedaikopi.model.Coffee
import com.dicoding.kedaikopi.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailFavoriteViewModel(
    private val repository: CoffeeRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Coffee>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Coffee>>
        get() = _uiState

    fun getCoffeeById(coffeeId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getCoffeeById(coffeeId))
        }
    }

    fun clear() {
        viewModelScope.launch {
            repository.clear()
        }
    }
}