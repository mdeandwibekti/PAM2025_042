package com.example.shoppeclonee.viewmodel.provider

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.CartItem
import com.example.shoppeclonee.repositori.AuthRepository
import com.example.shoppeclonee.repositori.CartRepository
import com.example.shoppeclonee.repositori.ShoppeCloneApp.Companion.authVM
import kotlinx.coroutines.launch

class CartViewModel(
    private val repo: CartRepository = CartRepository(),
) : ViewModel() {

    private var authVM: AuthViewModel? = null
    val cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val message = mutableStateOf("")
    val cart = mutableStateOf<List<CartItem>>(emptyList())

    fun setAuth(authViewModel: AuthViewModel) {
        this.authVM = authViewModel
    }
    fun loadCart(token: String) = viewModelScope.launch {
        cartItems.value = repo.getCart(token)
    }

    fun addToCart(authVM: AuthViewModel, productId: Int, quantity: Int = 1) = viewModelScope.launch {
        val token = authVM.token.value
        if (token.isNullOrBlank()) {
            message.value = "Silahkan login terlebih dahulu"
            return@launch
        }

        try {
            repo.addToCart(token, productId, quantity)
            message.value = "Berhasil ditambah ke keranjang"
            loadCart(token)
        } catch (e: Exception) {
            // Ini akan menangkap 400 Bad Request dan menampilkannya
            message.value = "Gagal: ${e.localizedMessage}"
        }
    }


    fun loadCart() = viewModelScope.launch {
        val token = authVM?.token?.value ?: return@launch
        cartItems.value = repo.getCart(token)
    }

    fun getCart() {
        viewModelScope.launch {
            try {
                val token = authVM?.token?.value ?: return@launch
                cart.value = repo.getCart(token)
            } catch (e: Exception) {
                cart.value = emptyList()
            }
        }
    }

    fun remove(id: Int) = viewModelScope.launch {
        val token = authVM?.token?.value ?: return@launch
        repo.removeItem(token, id)
        loadCart()
    }
}

