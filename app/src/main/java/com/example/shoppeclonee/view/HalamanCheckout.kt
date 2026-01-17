package com.example.shoppeclonee.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppeclonee.repositori.ShoppeCloneApp.Companion.authVM
import com.example.shoppeclonee.viewmodel.provider.CartViewModel
import com.example.shoppeclonee.viewmodel.provider.OrderViewModel

@Composable
fun HalamanCheckout(
    token: String,
    onOrderCreated: () -> Unit,
    onBack: () -> Unit,
    cartVm: CartViewModel = viewModel(),
    orderVm: OrderViewModel = viewModel()
) {
    val token = authVM.token.value ?: return
    val userId = authVM.user.value?.id ?: return


    // 🔥 LOAD CART
    LaunchedEffect(token, userId) {
        cartVm.loadCart(token, userId)
    }


    val cartItems by cartVm.cartItems.collectAsState()
    val totalPrice by cartVm.totalPrice.collectAsState()
    val message by orderVm.message.collectAsState()


    Scaffold(
        topBar = {
            TopAppBarLokalku(
                title = "Checkout",
                onBack = onBack
            )
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text(
                text = "Ringkasan Pesanan",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

            if (cartItems.isEmpty()) {
                Text("Keranjang kosong")
                return@Column
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = cartItems,
                    key = { it.id }
                ) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(text = item.product.name)
                        Text(
                            text = "${item.quantity} x Rp ${item.product.price}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Divider()
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Total: Rp $totalPrice",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = cartItems.isNotEmpty(),
                onClick = {
                    orderVm.createOrder(
                        token = token,
                        onSuccess = {
                            onOrderCreated()
                        }
                    )
                }
            ) {
                Text("Buat Order")
            }

            message?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
