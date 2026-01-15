package com.example.shoppeclonee.viewmodel.provider

import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.Product
import com.example.shoppeclonee.repositori.ProductRepository
import kotlinx.coroutines.launch

class SellerViewModel(
    private val repo: ProductRepository,
    private val authVM: AuthViewModel
) : ViewModel() {

    val myProducts = MutableLiveData<List<Product>>()
    val message = MutableLiveData<String?>()

    fun loadSellerProducts() {
        viewModelScope.launch {
            try {
                val token = authVM.token.value ?: return@launch
                myProducts.value = repo.getSellerProducts(token)
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }



    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            try {
                val token = authVM.token.value ?: return@launch
                repo.deleteProduct(token, id)
                loadSellerProducts()
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }
}

