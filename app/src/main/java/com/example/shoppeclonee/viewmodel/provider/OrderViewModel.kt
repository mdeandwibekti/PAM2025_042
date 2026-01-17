package com.example.shoppeclonee.viewmodel.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppeclonee.ContainerApp
import com.example.shoppeclonee.modeldata.Order
import com.example.shoppeclonee.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    // 🔥 repository langsung di sini (TANPA FACTORY)
    private val repository = OrderRepository(
        ContainerApp.instance.orderApi
    )

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun loadOrders(token: String) {
        viewModelScope.launch {
            try {
                val orders = repository.getOrders("Bearer $token")
                if (orders != null) {
                    _orders.value = orders
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }


    fun createOrder(token: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.createOrder("Bearer $token")
                _message.value = "Order berhasil dibuat"
                onSuccess()
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }
}
