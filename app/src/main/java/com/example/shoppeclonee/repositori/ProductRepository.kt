package com.example.shoppeclonee.repositori

class ProductRepository(
    private val container: ContainerApp = ContainerApp.instance
) {

    suspend fun getProducts() =
        container.productApi.getProducts()

    suspend fun getProductById(id: Int) =
        container.productApi.getProductById(id)

    suspend fun createProduct(
        token: String,
        name: String,
        price: Int,
        stock: Int,
        description: String
    ) = container.productApi.createProduct(
        token = "Bearer $token",
        body = mapOf(
            "name" to name,
            "price" to price,
            "stock" to stock,
            "description" to description
        )
    )

    suspend fun getAllProducts() =
        container.productApi.getAllProducts()

    suspend fun addProduct(
        token: String,
        body: Map<String, Any>
    ) = container.productApi.createProduct(token, body)


    suspend fun updateProduct(
        token: String,
        id: Int,
        body: Map<String, Any?>
    ) =
        container.productApi.updateProduct(
            token = token,
            id = id,
            body = body
        )

    suspend fun deleteProduct(token: String, id: Int) =
        container.productApi.deleteProduct(
            token = token,
            id = id
        )
}
