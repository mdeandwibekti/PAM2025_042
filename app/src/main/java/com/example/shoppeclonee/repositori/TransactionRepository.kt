package com.example.shoppeclonee.repositori

import com.example.shoppeclonee.ContainerApp

class TransactionRepository(
    private val container: ContainerApp = ContainerApp.instance
) {

    suspend fun createTransaction(
        token: String,
        orderId: Int,
        method: String
    ) =
        container.transactionApi.createTransaction(
            token,
            mapOf(
                "order_id" to orderId,
                "payment_method" to method
            )
        )

    suspend fun getTransactions(token: String) =
        container.transactionApi.getTransactions(token)
}
