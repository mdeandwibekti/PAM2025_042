package com.example.shoppeclonee.viewmodel.provider

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.CartItem
import com.example.shoppeclonee.repositori.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val repo: CartRepository = CartRepository()
) : ViewModel() {

    val cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val message = mutableStateOf("")

    fun loadCart(token: String) = viewModelScope.launch {
        cartItems.value = repo.getCart(token)
    }

    fun addToCart(token: String, productId: Int) = viewModelScope.launch {
        repo.addToCart(token, productId, 1)
        loadCart(token)
    }

    fun remove(token: String, id: Int) = viewModelScope.launch {
        repo.removeItem(token, id)
        loadCart(token)
    }
}

