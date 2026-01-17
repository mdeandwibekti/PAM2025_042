package com.example.shoppeclonee.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shoppeclonee.uicontroller.route.DestinasiCheckout
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel
import com.example.shoppeclonee.viewmodel.provider.CartViewModel

@Composable
fun HalamanCart(
    navController: NavHostController,
    authVM: AuthViewModel,
    cartVM: CartViewModel = viewModel()
) {
    // 🔥 LiveData → Compose State
    val token = authVM.token.value ?: return
    val userId = authVM.user.value?.id ?: return


    if (token.isNullOrEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Silakan login terlebih dahulu")
        }
        return
    }

    LaunchedEffect(token, userId) {
        cartVM.loadCart(token, userId)
    }


    val cartItems by cartVM.cartItems.collectAsState()
    val total by cartVM.totalPrice.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarLokalku(
                title = "Keranjang",
                onBack = { navController.popBackStack() }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            if (cartItems.isEmpty()) {
                Text("Keranjang masih kosong")
                return@Column
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = cartItems,
                    key = { it.id }
                ) { item ->

                    val price = item.product.price.toInt() // 🔥 pastikan Int

                    Column(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = item.product.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Qty: ${item.quantity}")
                        Text("Harga: Rp ${price * item.quantity}")

                        Spacer(Modifier.height(8.dp))

                        Row {
                            Button(
                                onClick = {
                                    cartVM.updateQuantity(
                                        token = token,
                                        userId = userId,
                                        cartId = item.id,
                                        quantity = item.quantity + 1
                                    )

                                }
                            ) {
                                Text("+")
                            }

                            Spacer(Modifier.width(8.dp))

                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                onClick = {
                                    cartVM.removeItem(
                                        token = token,
                                        userId = userId,
                                        cartId = item.id
                                    )

                                }
                            ) {
                                Text("Hapus")
                            }
                        }

                        Divider(Modifier.padding(top = 8.dp))
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Total: Rp $total",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate(DestinasiCheckout.route)
                }
            ) {
                Text("Checkout")
            }
        }
    }
}
