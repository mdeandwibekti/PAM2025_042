package com.example.shoppeclonee.apiservice

import com.example.shoppeclonee.modeldata.Product
import com.example.shoppeclonee.modeldata.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// ================= RESPONSE =================

data class BaseResponse(
    val status: Boolean,
    val message: String,
    val token: String?, // 🔥 WAJIB
    val user: User?
)

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val token: String?, // 🔥 WAJIB
    val user: User?
)

// ================= API =================

interface ServiceApiUser {

    @POST("api/users/register")
    suspend fun register(
        @Body body: Map<String, String>
    ): BaseResponse

    @POST("api/users/login")
    suspend fun login(
        @Body body: Map<String, String>
    ): LoginResponse

    @GET("api/users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): User

    @GET("api/products")
    suspend fun getProducts(): List<Product>

    @GET("api/products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): Product

    @POST("api/products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body body: Map<String, Any?>
    ): BaseResponse

    @GET("api/products")
    suspend fun getAllProducts(): List<Product>

    @PUT("api/products/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): BaseResponse

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): BaseResponse
}
