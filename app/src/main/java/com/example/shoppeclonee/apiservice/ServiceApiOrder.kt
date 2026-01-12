package com.example.shoppeclonee.apiservice


import com.example.shoppeclonee.modeldata.Order
import retrofit2.http.*


interface ServiceApiOrder {

    @POST("api/orders")
    suspend fun createOrder(
        @Header("Authorization") token: String
    ): BaseResponse

    @GET("api/orders")
    suspend fun getOrders(
        @Header("Authorization") token: String
    ): List<Order>
}

