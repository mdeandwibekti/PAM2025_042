package com.example.shoppeclonee.repositori

import com.example.shoppeclonee.ContainerApp
import com.example.shoppeclonee.apiservice.BaseResponse
import com.example.shoppeclonee.modeldata.Product

class ProductRepository {

    private val api = ContainerApp.instance.productApi

    suspend fun getAllProducts(): List<Product> {
        return api.getAllProducts().data
    }

    suspend fun getProductById(id: Int): Product {
        return api.getProductById(id).data.first()
    }

    suspend fun createProduct(
        token: String,
        name: String,
        price: Int,
        stock: Int,
        description: String
    ): BaseResponse {
        return api.createProduct(
            "Bearer $token",
            mapOf(
                "name" to name,
                "price" to price,
                "stock" to stock,
                "description" to description
            )
        )
    }

    suspend fun updateProduct(
        token: String,
        id: Int,
        name: String,
        price: Int,
        stock: Int,
        description: String
    ): BaseResponse {
        return api.updateProduct(
            "Bearer $token",
            id,
            mapOf(
                "name" to name,
                "price" to price,
                "stock" to stock,
                "description" to description
            )
        )
    }

    suspend fun deleteProduct(token: String, id: Int): BaseResponse {
        return api.deleteProduct("Bearer $token", id)
    }
}
