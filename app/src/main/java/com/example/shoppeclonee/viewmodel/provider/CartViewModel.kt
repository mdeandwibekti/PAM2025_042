package com.example.shoppeclonee.viewmodel.provider

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.CartItem
import com.example.shoppeclonee.repositori.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val repo: CartRepository = CartRepository(),
) : ViewModel() {

    private var authVM: AuthViewModel? = null

    var cartItems = mutableStateOf<List<CartItem>>(emptyList())
        private set
    val message = mutableStateOf("")
    val cart = mutableStateOf<List<CartItem>>(emptyList())


    fun setAuth(authViewModel: AuthViewModel) {
        this.authVM = authViewModel
    }


    // Di CartViewModel.kt
    // D:/.../viewmodel/provider/CartViewModel.kt

    // D:/.../viewmodel/provider/CartViewModel.kt

    // Lokasi: app/src/main/java/com/example/shoppeclonee/viewmodel/provider/CartViewModel.kt

    // D:/.../viewmodel/provider/CartViewModel.kt

    // Inisialisasi awal HARUS list kosong agar .isEmpty() tidak crash

    fun loadCart(token: String) = viewModelScope.launch {
        try {
            cartItems.value = repo.getCart(token)
        } catch (e: Exception) {
            Log.e("CartVM", "Gagal load: ${e.message}")
        }
    }

    fun addToCart(
        authVM: AuthViewModel,
        productId: Int,
        quantity: Int = 1,
        onSuccess: () -> Unit = {}
    ) = viewModelScope.launch {
        val token = authVM.token.value
        if (token.isNullOrBlank()) {
            message.value = "Silahkan login terlebih dahulu"
            return@launch
        }

        try {
            // 1. Tambah produk ke database backend
            repo.addToCart(token, productId, quantity)

            // 2. REFRESH DATA secara sinkron (tunggu sampai selesai)
            // Kita panggil repo langsung di sini agar datanya update dulu
            val updatedCart = repo.getCart(token)
            cartItems.value = updatedCart

            message.value = "Berhasil ditambah ke keranjang"

            // 3. Panggil navigasi SETELAH data diperbarui
            onSuccess()

        } catch (e: Exception) {
            Log.e("CartViewModel", "Error: ${e.message}")
            message.value = "Gagal: ${e.localizedMessage}"
        }
    }

    fun getCart() {
        viewModelScope.launch {
            try {
                val token = authVM?.token?.value ?: return@launch
                // ✅ Update variabel yang sama (cartItems)
                cartItems.value = repo.getCart(token)
            } catch (e: Exception) {
                cartItems.value = emptyList()
            }
        }
    }

    fun remove(id: Int) = viewModelScope.launch {
        val token = authVM?.token?.value ?: return@launch
        try {
            repo.removeItem(token, id)
            getCart()
        } catch (e: Exception) {
            Log.e("CartVM", "Gagal hapus")
        }
    }
}

