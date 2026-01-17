package com.example.shoppeclonee.modeldata


data class CartResponse(
    val items: List<CartItem>,
    val total: Int
)

data class BaseResponses<T>(
    val status: Boolean,
    val message: String,
    val data: T
)

data class CartItem(
    val id: Int,
    val quantity: Int,
    val subtotal: Int,
    val product: Product
)

data class AddToCartRequest(
    val product_id: Int,
    val quantity: Int
)

data class UpdateCartQtyRequest(
    val quantity: Int
)