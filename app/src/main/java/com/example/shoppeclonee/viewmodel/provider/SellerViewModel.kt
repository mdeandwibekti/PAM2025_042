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
                myProducts.value = repo.getProducts()
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
            repo.addProduct(
                token,
                mapOf(
                    "name" to name,
                    "price" to price,
                    "stock" to stock,
                    "description" to description
                )
            )
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
