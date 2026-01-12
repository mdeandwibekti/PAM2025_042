package com.example.shoppeclonee.viewmodel.provider


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.Order
import com.example.shoppeclonee.repositori.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repo: OrderRepository = OrderRepository()
) : ViewModel() {

    val orders = mutableStateOf<List<Order>>(emptyList())
    val message = mutableStateOf("")

    fun loadOrders(token: String) = viewModelScope.launch {
        orders.value = repo.getOrders(token)
    }

    fun checkout(token: String) = viewModelScope.launch {
        val res = repo.createOrder(token)
        message.value = res.message
    }
}

