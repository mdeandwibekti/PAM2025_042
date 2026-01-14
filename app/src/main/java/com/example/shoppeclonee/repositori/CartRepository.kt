package com.example.shoppeclonee.repositori

import com.example.shoppeclonee.ContainerApp

class CartRepository(
    private val container: ContainerApp = ContainerApp.instance
) {

    private val api = ContainerApp.instance.cartApi


    suspend fun getCart(token: String) =
        container.cartApi.getCart(token)

    suspend fun addToCart(
        token: String,
        productId: Int,
        quantity: Int
    ) {
        api.addToCart(
            token = "Bearer $token",
            body = mapOf(
                "product_id" to productId,
                "quantity" to quantity
            )
        )
    }

    suspend fun updateQty(token: String, id: Int, qty: Int) =
        container.cartApi.updateQty(
            token,
            id,
            mapOf("qty" to qty)
        )

    suspend fun removeItem(token: String, id: Int) =
        container.cartApi.removeItem(token, id)
}
