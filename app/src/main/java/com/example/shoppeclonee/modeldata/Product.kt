package com.example.shoppeclonee.modeldata

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    val price: String,              // 🔥 HARUS Int
    val stock: Int,
    val description: String?,
    val image: String?,

    @SerializedName("seller_id")
    val sellerId: Int
)
