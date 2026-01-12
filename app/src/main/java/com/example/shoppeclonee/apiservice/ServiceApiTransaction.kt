package com.example.shoppeclonee.apiservice


import com.example.shoppeclonee.modeldata.Transaction
import retrofit2.http.*

interface ServiceApiTransaction {

    @POST("api/transactions")
    suspend fun createTransaction(
        @Header("Authorization") token: String,
        @Body body: Map<String, Any>
    ): BaseResponse

    @GET("api/transactions")
    suspend fun getTransactions(
        @Header("Authorization") token: String
    ): List<Transaction>
}
