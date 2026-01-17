package com.example.shoppeclonee.apiservice

import com.example.shoppeclonee.modeldata.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    // Di ServiceApiProduct.kt
    @Multipart
    @POST("api/products")
    suspend fun createProduct(
        @Header("Authorization") token: String,

        @Part("name") name: RequestBody,
        @Part("price") price: Int,
        @Part("stock") stock: Int,
        @Part("description") description: RequestBody?,
        @Part("category") category: RequestBody,

        @Part image: MultipartBody.Part?
    ): BaseResponse



    @Multipart
    @PUT("api/products/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("price") price: Int,
        @Part("stock") stock: Int,
        @Part("description") description: RequestBody?,
        @Part image: MultipartBody.Part?
    ): BaseResponse

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): BaseResponse

    @GET("api/products/my")
    suspend fun getSellerProducts(
        @Header("Authorization") token: String
    ): List<Product>




}

