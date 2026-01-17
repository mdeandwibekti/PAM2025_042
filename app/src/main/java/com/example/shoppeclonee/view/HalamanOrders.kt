package com.example.shoppeclonee.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel
import com.example.shoppeclonee.viewmodel.provider.OrderViewModel

@Composable
fun HalamanOrder(
    onBack: () -> Unit,
    authVm: AuthViewModel = viewModel(),
    orderVm: OrderViewModel = viewModel()
) {

    val token = authVm.token.value

    LaunchedEffect(token) {
        if (!token.isNullOrEmpty()) {
            orderVm.loadOrders(token)
        }
    }

    val orders by orderVm.orders.collectAsState()

    Scaffold(
        topBar = { TopAppBarLokalku("Pesanan", onBack) }
    ) { pad ->
        LazyColumn(
            modifier = Modifier.padding(pad)
        ) {
            items(orders) { order ->
                Text("Order ID: ${order.id}")
            }
        }
    }
}

