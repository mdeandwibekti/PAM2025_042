package com.example.shoppeclonee.viewmodel.provider

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppeclonee.modeldata.Transaction
import com.example.shoppeclonee.repositori.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repo: TransactionRepository = TransactionRepository()
) : ViewModel() {

    val transactions = mutableStateOf<List<Transaction>>(emptyList())
    val message = mutableStateOf("")

    fun loadTransactions(token: String) = viewModelScope.launch {
        transactions.value = repo.getTransactions(token)
    }
}
