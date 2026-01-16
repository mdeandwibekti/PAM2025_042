package com.example.shoppeclonee.apiservice

import com.example.shoppeclonee.modeldata.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

data class ProductRequest(
    val name: String,
    val price: Int,
    val stock: Int,
    val description: String?,
    val image: String
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
}

