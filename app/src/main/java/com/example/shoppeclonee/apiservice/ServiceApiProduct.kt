package com.example.shoppeclonee.apiservice

import com.example.shoppeclonee.modeldata.Product
import retrofit2.http.*

// ================= RESPONSE =================




data class SingleProductResponse(
    val msg: String,
    val data: Product
)


// ================= API =================

interface ServiceApiProduct {

    @GET("api/products")
    suspend fun getAllProducts(): List<Product>


    @GET("api/products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): SingleProductResponse


    @POST("api/products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body body: ProductRequest
    ): BaseResponse

    @PUT("api/products/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: ProductRequest
    ): BaseResponse

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): BaseResponse

    @GET("api/seller/products")
    suspend fun getSellerProducts(
        @Header("Authorization") token: String
    ): List<Product>


}

