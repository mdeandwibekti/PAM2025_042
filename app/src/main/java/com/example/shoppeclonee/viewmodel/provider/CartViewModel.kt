package com.example.shoppeclonee.viewmodel.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppeclonee.ContainerApp
import com.example.shoppeclonee.modeldata.CartItem
import com.example.shoppeclonee.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val repository = CartRepository(
        ContainerApp.instance.cartApi
    )

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> = _totalPrice



    fun addToCart(
        token: String,
        userId: Int,
        productId: Int,
        quantity: Int
    ) {
        viewModelScope.launch {
            try {
                repository.addToCart(token, productId, quantity)
                loadCart(token, userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun loadCart(token: String, userId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCart(token, userId)
                _cartItems.value = response.items
                _totalPrice.value = response.total
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun updateQuantity(
        token: String,
        userId: Int,
        cartId: Int,
        quantity: Int
    ) {
        viewModelScope.launch {
            try {
                repository.updateQuantity(token, cartId, quantity)
                loadCart(token, userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun removeItem(
        token: String,
        userId: Int,
        cartId: Int
    ) {
        viewModelScope.launch {
            try {
                repository.removeItem(token, cartId)
                loadCart(token, userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

