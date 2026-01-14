package com.example.shoppeclonee.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.example.shoppeclonee.repositori.ShoppeCloneApp
import com.example.shoppeclonee.uicontroller.route.DestinasiCart
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel
import com.example.shoppeclonee.viewmodel.provider.HomeViewModel
import com.example.shoppeclonee.viewmodel.provider.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    navController: NavHostController,
    onProductClick: (Int) -> Unit,
    vm: HomeViewModel = viewModel(),
    cartVM: CartViewModel = viewModel(),
    authVm: AuthViewModel = viewModel(),
    productVm: HomeViewModel = viewModel()

) {

    var searchText by remember { mutableStateOf("") }

    val authVm: AuthViewModel = viewModel()

    LaunchedEffect(Unit) {
        productVm.loadProducts()
    }
    LazyColumn {
        items(productVm.products.value) { product ->
            Text(product.name)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text("Cari produk UMKM…") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Search, null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(DestinasiCart.route) }
                    ) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }
                }
            )
        },
        bottomBar = {
            BottomBarLokalku(navController)
        }
    ) { pad ->

        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .padding(8.dp)
                .fillMaxSize()
        ) {

            // ================= PROMO BANNER =================
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "PROMO UMKM\nDISKON HARI INI",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ================= GRID PRODUK =================
            items(vm.products.value?.chunked(2) ?: emptyList()) { row ->

                Row(modifier = Modifier.fillMaxWidth()) {

                    row.forEach { p ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(6.dp)
                                .clickable { onProductClick(p.id) },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Foto Produk")
                                }

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    p.name,
                                    maxLines = 2,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        "4.8 • 10rb+",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    "Rp ${p.price}",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(6.dp))

                                // ✅ FIX ERROR DI SINI
                                Button(
                                    onClick = {
                                        cartVM.addToCart(p.id)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.ShoppingCart,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text("Tambah")
                                }

                            }
                        }
                    }

                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
