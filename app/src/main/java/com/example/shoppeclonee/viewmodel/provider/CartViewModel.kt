package com.example.shoppeclonee.viewmodel.provider

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.CartItem
import com.example.shoppeclonee.repositori.AuthRepository
import com.example.shoppeclonee.repositori.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val repo: CartRepository = CartRepository(),
    private val authVM: AuthViewModel = AuthViewModel()) : ViewModel() {

    val cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val message = mutableStateOf("")
    val cart = mutableStateOf<List<CartItem>>(emptyList())

    fun loadCart(token: String) = viewModelScope.launch {
        cartItems.value = repo.getCart(token)
    }

    fun addToCart(productId: Int, quantity: Int = 1) = viewModelScope.launch {
        val token = authVM.token.value ?: return@launch
        repo.addToCart(token, productId, quantity)
        loadCart(token)
    }


    fun loadCart() = viewModelScope.launch {
        val token = authVM.token.value ?: return@launch
        cartItems.value = repo.getCart(token)
    }

    fun getCart() {
        viewModelScope.launch {
            try {
                val token = authVM.token.value ?: return@launch
                cart.value = repo.getCart(token)
            } catch (e: Exception) {
                cart.value = emptyList()
            }
        }
    }

    fun remove(id: Int) = viewModelScope.launch {
        val token = authVM.token.value ?: return@launch
        repo.removeItem(token, id)
        loadCart()
    }
}

