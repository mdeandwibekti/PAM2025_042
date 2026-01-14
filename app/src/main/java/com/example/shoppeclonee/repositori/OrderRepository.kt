package com.example.shoppeclonee.repositori

import com.example.shoppeclonee.ContainerApp
import com.example.shoppeclonee.apiservice.BaseResponse
import com.example.shoppeclonee.apiservice.OrderResponse

class OrderRepository {

    private val api = ContainerApp.instance.orderApi

    suspend fun createOrder(token: String): BaseResponse {
        return api.createOrder("Bearer $token")
    }

    suspend fun getOrders(token: String): OrderResponse {
        return api.getOrders("Bearer $token")
    }
}

