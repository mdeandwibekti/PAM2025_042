package com.example.shoppeclonee.modeldata

data class Order(
    val id: Int,
    val total: Int,
    val status: String,
    val created_at: String
)
