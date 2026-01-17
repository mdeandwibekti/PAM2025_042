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
    var showSheet by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantity by remember { mutableIntStateOf(1) }

    val sheetState = rememberModalBottomSheetState()

    val products by vm.products.observeAsState(emptyList())
    val loading by vm.loading.observeAsState(false)


    LaunchedEffect(Unit) {
        vm.loadProducts()
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
                        shape = MaterialTheme.shapes.large,
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

        /* =================== BOTTOM SHEET =================== */
        if (showSheet && selectedProduct != null) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        selectedProduct!!.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Rp ${selectedProduct!!.price}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(24.dp))

                    Text("Jumlah", fontWeight = FontWeight.Medium)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledIconButton(onClick = { if (quantity > 1) quantity-- }) {
                            Text("-")
                        }

                        Text(
                            quantity.toString(),
                            modifier = Modifier.padding(horizontal = 32.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        FilledIconButton(onClick = { quantity++ }) {
                            Text("+")
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            cartVM.addToCart(
                                token = authVM.token.value!!,
                                userId = authVM.user.value!!.id,
                                productId = selectedProduct!!.id,
                                quantity = quantity
                            )

                            navController.navigate(DestinasiCart.route)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text("Masukkan ke Keranjang")
                    }
                }
            }
        }

        /* =================== CONTENT =================== */
        if (loading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(pad),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .padding(horizontal = 12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = MaterialTheme.shapes.large
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
                                    .clickable { onProductClick(p.id) },
                                shape = MaterialTheme.shapes.large,
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(Modifier.padding(12.dp)) {

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp),
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
                                            Icon(
                                                Icons.Default.Star,
                                                null,
                                                modifier = Modifier.size(48.dp)
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        p.name,
                                        maxLines = 1,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        "Rp ${p.price}",
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            selectedProduct = p
                                            quantity = 1
                                            showSheet = true
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = MaterialTheme.shapes.large
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
