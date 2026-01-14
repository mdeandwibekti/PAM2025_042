package com.example.shoppeclonee.viewmodel.provider


import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.Product
import com.example.shoppeclonee.repositori.ProductRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repo = ProductRepository()

    val loading = MutableLiveData(false)
    val products = MutableLiveData<List<Product>>(emptyList())
    val message = MutableLiveData<String?>()

    fun loadProducts() {
        viewModelScope.launch {
            try {
                products.value = repo.getAllProducts()
            } catch (e: Exception) {
                products.value = emptyList()
            }
        }
    }


}
