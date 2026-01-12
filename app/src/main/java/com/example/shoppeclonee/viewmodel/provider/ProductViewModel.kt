package com.example.shoppeclonee.viewmodel.provider

import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.Product
import com.example.shoppeclonee.repositori.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {


    private val repo = ProductRepository()

    // sementara (nanti ambil dari AuthViewModel)
    private val token = "Bearer TOKEN_LOGIN_KAMU"

    val loading = MutableLiveData(false)
    val products = MutableLiveData<List<Product>>()
    val product = MutableLiveData<Product?>()
    val message = MutableLiveData<String?>()

    fun getProducts() {
        viewModelScope.launch {
            loading.value = true
            try {
                products.value = repo.getProducts()
            } catch (e: Exception) {
                message.value = e.message
            } finally {
                loading.value = false
            }
        }
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            try {
                product.value = repo.getProductById(id)
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }


    fun getProductDetail(id: Int) {
        viewModelScope.launch {
            product.value = repo.getProductById(id)
        }
    }


    fun addProduct(body: Map<String, Any?>) {
        viewModelScope.launch {
            try {
                message.value = repo.addProduct(token, body as Map<String, Any>).message
                getProducts() // 🔥 refresh home
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }

    fun updateProduct(id: Int, body: Map<String, Any?>) {
        viewModelScope.launch {
            try {
                message.value = repo.updateProduct(token, id, body).message
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            try {
                message.value = repo.deleteProduct(token, id).message
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }


}
