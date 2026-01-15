package com.example.shoppeclonee.view

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.shoppeclonee.R
import com.example.shoppeclonee.repositori.ShoppeCloneApp
import com.example.shoppeclonee.uicontroller.route.DestinasiCart
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel
import com.example.shoppeclonee.viewmodel.provider.HomeViewModel
import com.example.shoppeclonee.viewmodel.provider.CartViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    navController: NavHostController,
    onProductClick: (Int) -> Unit,
    authVM: AuthViewModel,
    vm: HomeViewModel = viewModel(),
    cartVM: CartViewModel = viewModel()
) {

    var searchText by remember { mutableStateOf("") }

    // 🔥 Observe LiveData dengan benar di Compose
    val products by vm.products.observeAsState(emptyList())
    val loading by vm.loading.observeAsState(false)
    val message by vm.message.observeAsState()

    LaunchedEffect(Unit) {
        vm.loadProducts()
    }
    LaunchedEffect(authVM) {
        cartVM.setAuth(authVM)
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
                        leadingIcon = { Icon(Icons.Default.Search, null) },
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
        bottomBar = { BottomBarLokalku(navController) }
    ) { pad ->

        if (loading) {
            // 🔥 Tampilkan loading indicator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (products.isEmpty()) {
            // 🔥 Tampilkan pesan jika tidak ada produk
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Tidak ada produk", style = MaterialTheme.typography.bodyLarge)
                    if (!message.isNullOrBlank()) {
                        Text(
                            message!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(onClick = { vm.loadProducts() }) {
                        Text("Coba Lagi")
                    }
                }
            }
        } else {
            // 🔥 Tampilkan produk
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .padding(8.dp)
            ) {

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
                                "SELAMAT DATANG DI PASAR LOKALKU",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(all = 50.dp),
                            )
                        }
                    }
                }

                items(products.chunked(2)) { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {

                        row.forEach { p ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(6.dp)
                                    .clickable { onProductClick(p.id) }
                            ) {
                                Column(Modifier.padding(8.dp)) {

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            modifier = Modifier.size(48.dp)
                                        )
                                    }

                                    Text(
                                        p.name,
                                        maxLines = 2,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        "Rp ${p.price}",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // ...
                                    Button(
                                        onClick = { cartVM.addToCart(authVM, p.id) }, // Lewatkan authVM
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Tambah")
                                    }
                                }
                            }
                        }

                        if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}


