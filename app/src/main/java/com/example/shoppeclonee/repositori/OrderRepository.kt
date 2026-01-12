package com.example.shoppeclonee.repositori

class OrderRepository(
    private val container: ContainerApp = ContainerApp.instance
) {

    suspend fun createOrder(token: String) =
        container.orderApi.createOrder(token)

    suspend fun getOrders(token: String) =
        container.orderApi.getOrders(token)
}
