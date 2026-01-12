package com.example.shoppeclonee.repositori

import com.example.shoppeclonee.apiservice.ServiceApiCart
import com.example.shoppeclonee.apiservice.ServiceApiOrder
import com.example.shoppeclonee.apiservice.ServiceApiTransaction
import com.example.shoppeclonee.apiservice.ServiceApiUser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContainerApp private constructor() {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ===== API SERVICE =====
    val userApi: ServiceApiUser by lazy {
        retrofit.create(ServiceApiUser::class.java)
    }

    val productApi: ServiceApiUser by lazy {
        retrofit.create(ServiceApiUser::class.java)
    }

    val cartApi: ServiceApiCart by lazy {
        retrofit.create(ServiceApiCart::class.java)
    }

    val orderApi: ServiceApiOrder by lazy {
        retrofit.create(ServiceApiOrder::class.java)
    }

    val transactionApi: ServiceApiTransaction by lazy {
        retrofit.create(ServiceApiTransaction::class.java)
    }

    companion object {
        val instance: ContainerApp by lazy { ContainerApp() }
    }
}
