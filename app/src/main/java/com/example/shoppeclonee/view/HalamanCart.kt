// app/src/main/java/com/example/shoppeclonee/view/HalamanCart.kt
package com.example.shoppeclonee.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel
import com.example.shoppeclonee.viewmodel.provider.CartViewModel

@Composable
fun HalamanCart(
    onCheckout: () -> Unit,
    onBack: () -> Unit,
    vm: CartViewModel = viewModel(),
    authVM: AuthViewModel
) {
    LaunchedEffect(Unit) {
        vm.getCart()
    }

    // ✅ Gunakan cartItems sesuai perbaikan di ViewModel
    val items = vm.cartItems.value

    Scaffold(
        topBar = { TopAppBarLokalku("Keranjang Belanja", onBack) }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (items.isEmpty()) {
                Box(Modifier.weight(1f).fillMaxWidth(), Alignment.Center) {
                    Text("Keranjang Kosong")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    // ✅ Tampilkan Nama Produk
                                    Text(
                                        text = item.product?.name ?: "Produk #${item.product_id}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    // ✅ Tampilkan Harga
                                    Text(
                                        text = "Rp ${item.product?.price ?: 0}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = "x${item.quantity}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                enabled = items.isNotEmpty()
            ) {
                Text("Lanjut ke Checkout")
            }
        }
    }
}