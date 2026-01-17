package com.example.shoppeclonee.apiservice

import com.example.shoppeclonee.modeldata.BaseResponse
import com.example.shoppeclonee.modeldata.BaseResponses
import com.example.shoppeclonee.modeldata.CartItem
import com.example.shoppeclonee.modeldata.CartResponse
import com.example.shoppeclonee.modeldata.UpdateCartQtyRequest
import retrofit2.http.*
import com.example.shoppeclonee.modeldata.*


    interface ServiceApiCart {

        @POST("cart")
        suspend fun addToCart(
            @Header("Authorization") token: String,
            @Body body: Map<String, Int>
        ): BaseResponses<CartItem>

        @GET("cart/user/{user_id}")
        suspend fun getCartByUser(
            @Header("Authorization") token: String,
            @Path("user_id") userId: Int
        ): CartResponse

        @PUT("cart/{id}")
        suspend fun updateQuantity(
            @Header("Authorization") token: String,
            @Path("id") id: Int,
            @Body body: Map<String, Int>
        ): BaseResponses<CartItem>

        @DELETE("cart/{id}")
        suspend fun removeFromCart(
            @Header("Authorization") token: String,
            @Path("id") id: Int
        ): BaseResponses<Any>

        @DELETE("cart/user/{user_id}")
        suspend fun clearCart(
            @Header("Authorization") token: String,
            @Path("user_id") userId: Int
        ): BaseResponse
    }

