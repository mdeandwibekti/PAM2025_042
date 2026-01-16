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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.shoppeclonee.uicontroller.route.DestinasiCart
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel
import com.example.shoppeclonee.viewmodel.provider.HomeViewModel
import com.example.shoppeclonee.viewmodel.provider.CartViewModel
import com.example.shoppeclonee.modeldata.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    navController: NavHostController,
    onProductClick: (Int) -> Unit,
    authVM: AuthViewModel,
    vm: HomeViewModel = viewModel(),
    cartVM: CartViewModel = viewModel(),
) {
    var searchText by remember { mutableStateOf("") }

    // State untuk BottomSheet Picker
    var showSheet by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantity by remember { mutableIntStateOf(1) }
    val sheetState = rememberModalBottomSheetState()

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
                    IconButton(onClick = { navController.navigate(DestinasiCart.route) }) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }
                }
            )
        },
        bottomBar = { BottomBarLokalku(navController) }
    ) { pad ->

        // --- BOTTOM SHEET (PICKER JUMLAH) ---
        if (showSheet && selectedProduct != null) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        text = selectedProduct?.name ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Harga: Rp ${selectedProduct?.price}",
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Tentukan Jumlah", fontWeight = FontWeight.Medium)

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledIconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.LightGray)
                        ) { Text("-", style = MaterialTheme.typography.titleLarge) }

                        Text(
                            text = quantity.toString(),
                            modifier = Modifier.padding(horizontal = 32.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        FilledIconButton(
                            onClick = { quantity++ },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.LightGray)
                        ) { Text("+", style = MaterialTheme.typography.titleLarge) }
                    }

                    Button(
                        onClick = {
                            cartVM.addToCart(
                                authVM = authVM,
                                productId = selectedProduct!!.id,
                                quantity = quantity,
                                onSuccess = {
                                    showSheet = false
                                    navController.navigate(DestinasiCart.route)
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Konfirmasi & Masuk Keranjang")
                    }
                }
            }
        }

        // --- LOADING & CONTENT ---
        if (loading) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(pad).padding(8.dp)) {
                item {
                    // Banner Selamat Datang
                    Card(
                        modifier = Modifier.fillMaxWidth().height(140.dp).padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "PASAR LOKALKU",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
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
                                Column(Modifier.padding(10.dp)) {
                                    // Di dalam Card produk HalamanHome.kt
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (!p.image.isNullOrEmpty()) {
                                            AsyncImage(
                                                model = p.image,
                                                contentDescription = p.name,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Icon(Icons.Default.Star, null, modifier = Modifier.size(40.dp))
                                        }
                                    }

                                    Text(p.name, maxLines = 1, fontWeight = FontWeight.Bold)
                                    Text("Rp ${p.price}", color = MaterialTheme.colorScheme.primary)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Di dalam HalamanHome.kt
                                    Button(
                                        onClick = {
                                            selectedProduct = p // 'p' adalah objek Product
                                            quantity = 1
                                            showSheet = true // Menampilkan Bottom Sheet untuk input jumlah
                                        },
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