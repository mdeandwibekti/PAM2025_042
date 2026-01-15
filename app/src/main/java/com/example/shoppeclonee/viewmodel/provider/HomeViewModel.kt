package com.example.shoppeclonee.viewmodel.provider

import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.Product
import com.example.shoppeclonee.repositori.ProductRepository
import kotlinx.coroutines.launch
import android.util.Log
import retrofit2.HttpException

class HomeViewModel : ViewModel() {
    private val repo = ProductRepository()

    val loading = MutableLiveData(false)
    val products = MutableLiveData<List<Product>>(emptyList())
    val message = MutableLiveData<String?>()

    init {
        loadProducts()
    }

    fun loadProducts() {
        loading.value = true
        viewModelScope.launch {
            try {
                val data = repo.getAllProducts()
                products.value = data
                Log.d("HomeViewModel", "✅ Berhasil load ${data.size} produk")
            } catch (e: HttpException) {
                products.value = emptyList()
                val errorMsg = when (e.code()) {
                    500 -> "Server Error (500) - Backend error, periksa server logs"
                    404 -> "Not Found (404) - Endpoint tidak ditemukan"
                    403 -> "Forbidden (403) - Akses ditolak"
                    401 -> "Unauthorized (401) - Token tidak valid"
                    else -> "HTTP Error ${e.code()} - ${e.message()}"
                }
                message.value = errorMsg
                Log.e("HomeViewModel", "❌ HTTP ${e.code()}: $errorMsg", e)
            } catch (e: Exception) {
                products.value = emptyList()
                message.value = e.message ?: "Gagal memuat produk"
                Log.e("HomeViewModel", "❌ Error loading products: ${e.javaClass.simpleName}", e)
            } finally {
                loading.value = false
            }
        }
    }


}
