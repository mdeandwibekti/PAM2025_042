package com.example.shoppeclonee.repository

import com.example.shoppeclonee.apiservice.ServiceApiCart
import com.example.shoppeclonee.modeldata.BaseResponses
import com.example.shoppeclonee.modeldata.CartItem
import com.example.shoppeclonee.modeldata.CartResponse

class CartRepository(
    private val api: ServiceApiCart
) {

    suspend fun addToCart(
        token: String,
        productId: Int,
        quantity: Int
    ): BaseResponses<CartItem> {

        val body = mapOf(
            "product_id" to productId,
            "quantity" to quantity
        )

        return api.addToCart(
            token = formatToken(token),
            body = body
        )
    }

    suspend fun getCart(
        token: String,
        userId: Int
    ): CartResponse {
        return api.getCartByUser(
            token = formatToken(token),
            userId = userId
        )
    }

    suspend fun updateQuantity(
        token: String,
        cartId: Int,
        quantity: Int
    ): BaseResponses<CartItem> {

        return api.updateQuantity(
            token = formatToken(token),
            id = cartId,
            body = mapOf("quantity" to quantity)
        )
    }

    suspend fun removeItem(
        token: String,
        cartId: Int
    ): BaseResponses<Any> {

        return api.removeFromCart(
            token = formatToken(token),
            id = cartId
        )
    }

    private fun formatToken(token: String): String {
        return if (token.startsWith("Bearer ")) token else "Bearer $token"
    }
}

