package com.dicoding.kedaikopi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kedaikopi.data.CoffeeRepository
import com.dicoding.kedaikopi.ui.screen.cart.CartViewModel
import com.dicoding.kedaikopi.ui.screen.detail.DetailFavoriteViewModel
import com.dicoding.kedaikopi.ui.screen.favorite.FavoriteViewModel

class ViewModelFactory(private val repository: CoffeeRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailFavoriteViewModel::class.java)) {
            return DetailFavoriteViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}