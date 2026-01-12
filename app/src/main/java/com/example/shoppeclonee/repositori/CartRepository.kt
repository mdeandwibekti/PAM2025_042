package com.example.shoppeclonee.repositori

class CartRepository(
    private val container: ContainerApp = ContainerApp.instance
) {

    suspend fun getCart(token: String) =
        container.cartApi.getCart(token)

    suspend fun addToCart(token: String, productId: Int, qty: Int) =
        container.cartApi.addToCart(
            token,
            mapOf("product_id" to productId, "qty" to qty)
        )

    suspend fun updateQty(token: String, id: Int, qty: Int) =
        container.cartApi.updateQty(
            token,
            id,
            mapOf("qty" to qty)
        )

    suspend fun removeItem(token: String, id: Int) =
        container.cartApi.removeItem(token, id)
}
