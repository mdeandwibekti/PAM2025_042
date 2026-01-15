package com.example.shoppeclonee.repositori

import com.example.shoppeclonee.ContainerApp
import com.example.shoppeclonee.apiservice.BaseResponse
import com.example.shoppeclonee.apiservice.ProductRequest
import com.example.shoppeclonee.modeldata.Product
import android.util.Log
import retrofit2.HttpException

class ProductRepository {

    private val api = ContainerApp.instance.productApi
    private val TAG = "ProductRepository"

    /* ================= GET ================= */

    suspend fun getAllProducts(): List<Product> {
        return try {
            Log.d(TAG, "📤 GET /api/products")
            val response = api.getAllProducts()
            Log.d(TAG, "✅ Berhasil menerima ${response.size} produk")
            response
        } catch (e: Exception) {
            Log.e(TAG, "❌ ${e.message}", e)
            emptyList()
        }
    }


    suspend fun getProductById(id: Int): Product {
        return api.getProductById(id).data
    }

    /* ================= CREATE ================= */

    suspend fun createProduct(
        token: String,
        name: String,
        price: Int,
        stock: Int,
        description: String?
    ): BaseResponse {

        val body = ProductRequest(
            name = name,
            price = price,
            stock = stock,
            description = description
        )

        return api.createProduct(
            token = "Bearer $token",
            body = body
        )
    }

    /* ================= UPDATE ================= */

    suspend fun updateProduct(
        token: String,
        id: Int,
        name: String,
        price: Int,
        stock: Int,
        description: String?
    ): BaseResponse {

        val body = ProductRequest(
            name = name,
            price = price,
            stock = stock,
            description = description
        )

        return api.updateProduct(
            token = "Bearer $token",
            id = id,
            body = body
        )
    }

    /* ================= DELETE ================= */

    suspend fun deleteProduct(
        token: String,
        id: Int
    ): BaseResponse {
        return api.deleteProduct(
            token = "Bearer $token",
            id = id
        )
    }
    suspend fun getSellerProducts(token: String): List<Product> {
        return api.getSellerProducts("Bearer $token")
    }

}
