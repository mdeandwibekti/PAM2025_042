package com.example.shoppeclonee.viewmodel.provider

import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.Product
import com.example.shoppeclonee.repositori.ProductRepository
import kotlinx.coroutines.launch

class SellerViewModel : ViewModel() {

    private val repo = ProductRepository()

    val myProducts = MutableLiveData<List<Product>>()
    val message = MutableLiveData<String?>()

    fun loadSellerProducts() {
        viewModelScope.launch {
            try {
                myProducts.value = repo.getAllProducts()
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }

    fun addProduct(
        token: String,
        name: String,
        price: Int,
        stock: Int,
        description: String
    ) {
        viewModelScope.launch {
            try {
                val response = repo.createProduct(
                    token,
                    name,
                    price,
                    stock,
                    description
                )
                message.value = response.message
                loadSellerProducts()
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }

    fun deleteProduct(token: String, id: Int) {
        viewModelScope.launch {
            try {
                repo.deleteProduct(token, id)
                loadSellerProducts()
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }
}
