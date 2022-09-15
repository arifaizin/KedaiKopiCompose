package com.dicoding.kedaikopi.di

import com.dicoding.kedaikopi.data.CoffeeRepository

object Injection {
    fun provideRepository(): CoffeeRepository {
        return CoffeeRepository.getInstance()
    }
}