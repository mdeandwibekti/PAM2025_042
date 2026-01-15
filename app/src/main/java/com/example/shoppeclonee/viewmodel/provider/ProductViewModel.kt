package com.example.shoppeclonee.viewmodel.provider

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppeclonee.modeldata.Product
import com.example.shoppeclonee.repositori.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repo: ProductRepository,
    private val authVM: AuthViewModel
) : ViewModel() {

    val products = mutableStateOf<List<Product>>(emptyList())
    val product = mutableStateOf<Product?>(null)

    val loading = mutableStateOf(false)
    val message = mutableStateOf<String?>(null)

    private val token: String?
        get() = authVM.token.value

    fun clearMessage() {
        message.value = null
    }

    // ================= LOAD ALL =================
    fun loadProducts() = viewModelScope.launch {
        loading.value = true
        clearMessage()
        try {
            products.value = repo.getAllProducts()
        } catch (e: Exception) {
            message.value = e.message ?: "Gagal memuat produk"
        } finally {
            loading.value = false
        }
    }

    // ================= GET BY ID =================
    fun getProductById(id: Int) = viewModelScope.launch {
        loading.value = true
        clearMessage()
        try {
            product.value = repo.getProductById(id)
        } catch (e: Exception) {
            message.value = "Produk tidak ditemukan"
        } finally {
            loading.value = false
        }
    }

    // ================= ADD =================
    fun addProduct(
        name: String,
        price: Int,
        stock: Int,
        description: String,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {

        if (token.isNullOrBlank()) {
            message.value = "Anda belum login"
            return@launch
        }

        loading.value = true
        clearMessage()

        try {
            repo.createProduct(token!!, name, price, stock, description)
            loadProducts()
            onSuccess()
        } catch (e: Exception) {
            message.value = e.message ?: "Gagal menyimpan produk"
        } finally {
            loading.value = false
        }
    }

    // ================= UPDATE =================
    fun updateProduct(
        id: Int,
        name: String,
        price: Int,
        stock: Int,
        description: String,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {

        if (token.isNullOrBlank()) {
            message.value = "Anda belum login"
            return@launch
        }

        loading.value = true
        clearMessage()

        try {
            repo.updateProduct(token!!, id, name, price, stock, description)
            loadProducts()
            onSuccess()
        } catch (e: Exception) {
            message.value = e.message ?: "Gagal update produk"
        } finally {
            loading.value = false
        }
    }

    // ================= DELETE =================
    fun deleteProduct(
        id: Int,
        onSuccess: () -> Unit = {}
    ) = viewModelScope.launch {

        if (token.isNullOrBlank()) {
            message.value = "Anda belum login"
            return@launch
        }

        loading.value = true
        clearMessage()

        try {
            repo.deleteProduct(token!!, id)
            loadProducts()
            onSuccess()
        } catch (e: Exception) {
            message.value = "Gagal hapus produk"
        } finally {
            loading.value = false
        }
    }
}
