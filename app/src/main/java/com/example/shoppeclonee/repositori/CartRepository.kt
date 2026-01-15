package com.example.shoppeclonee.repositori

import com.example.shoppeclonee.ContainerApp

class CartRepository(
    private val container: ContainerApp = ContainerApp.instance
) {

    private val api = ContainerApp.instance.cartApi


    suspend fun getCart(token: String) =
        container.cartApi.getCart(if (token.startsWith("Bearer ")) token else "Bearer $token")

    // D:/semester 5/PAM/shoppeclonee/app/src/main/java/com/example/shoppeclonee/repositori/CartRepository.kt

    suspend fun addToCart(token: String, productId: Int, quantity: Int) {
        val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
        api.addToCart(
            token = formattedToken,
            body = mapOf(
                "id" to productId, // Ganti ke 'id' jika 'product_id' gagal
                "qty" to quantity  // Ganti ke 'qty' jika 'quantity' gagal
            )
        )
    }

    suspend fun updateQty(token: String, id: Int, qty: Int) =
        container.cartApi.updateQty(
            if (token.startsWith("Bearer ")) token else "Bearer $token",
            id,
            mapOf("qty" to qty)
        )

    suspend fun removeItem(token: String, id: Int) =
        container.cartApi.removeItem(
            if (token.startsWith("Bearer ")) token else "Bearer $token",
            id
        )
}
