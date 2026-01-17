package com.example.shoppeclonee.repositori

import android.net.Uri
import com.example.shoppeclonee.ContainerApp
import com.example.shoppeclonee.apiservice.BaseResponse
import com.example.shoppeclonee.modeldata.Product
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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
        description: String?,
        imageUri: String // Menerima String dari ViewModel
    ): BaseResponse {

        // 1. Konversi Teks ke RequestBody agar bisa dikirim via Multipart
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = description?.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryPart = "General".toRequestBody("text/plain".toMediaTypeOrNull())

        // 2. Olah Gambar: Konversi String URI ke MultipartBody.Part
        var imagePart: MultipartBody.Part? = null
        if (imageUri.isNotEmpty()) {
            val uri = Uri.parse(imageUri)
            val file = File(uri.path ?: "") // Pastikan path file benar
            if (file.exists()) {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
        }

        return api.createProduct(
            token = "Bearer $token",
            name = namePart,
            price = price,
            stock = stock,
            description = descPart,
            category = categoryPart,
            image = imagePart
        )
    }

    suspend fun updateProduct(
        token: String,
        id: Int,
        name: String,
        price: Int,
        stock: Int,
        description: String?,
        imageUri: String // Menerima String dari ViewModel
    ): BaseResponse {

        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = description?.toRequestBody("text/plain".toMediaTypeOrNull())

        var imagePart: MultipartBody.Part? = null
        // Cek jika imageUri adalah path lokal (untuk upload ulang)
        if (imageUri.startsWith("content://") || imageUri.startsWith("file://")) {
            val uri = Uri.parse(imageUri)
            val file = File(uri.path ?: "")
            if (file.exists()) {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
        }

        return api.updateProduct(
            token = "Bearer $token",
            id = id,
            name = namePart,
            price = price,
            stock = stock,
            description = descPart,
            image = imagePart
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
