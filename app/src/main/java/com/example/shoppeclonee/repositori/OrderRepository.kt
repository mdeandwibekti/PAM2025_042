package com.example.shoppeclonee.repository

import com.example.shoppeclonee.apiservice.ServiceApiOrder
import com.example.shoppeclonee.modeldata.Order

class OrderRepository(
    private val api: ServiceApiOrder
) {

    suspend fun getOrders(token: String): List<Order>? {
        val response = api.getOrders("Bearer $token")
        return response.data
    }

    suspend fun createOrder(token: String) =
        api.createOrder("Bearer $token")
}
