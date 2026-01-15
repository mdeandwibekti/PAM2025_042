package com.example.shoppeclonee.apiservice


import com.example.shoppeclonee.modeldata.CartItem
import retrofit2.http.*


interface ServiceApiCart {

    @GET("api/cart")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): List<CartItem>

    @POST("api/cart")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body body: Map<String, Int>
    ): BaseResponse

    @PUT("api/cart/{id}")
    suspend fun updateQty(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: Map<String, Int>
    ): BaseResponse

    @DELETE("api/cart/{id}")
    suspend fun removeItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): BaseResponse
}

