package com.example.shoppeclonee.viewmodel.provider


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppeclonee.repositori.ProductRepository

class SellerViewModelFactory(
    private val repo: ProductRepository,
    private val authVM: AuthViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SellerViewModel(repo, authVM) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
