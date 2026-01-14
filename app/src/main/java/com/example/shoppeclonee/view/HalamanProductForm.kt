package com.example.shoppeclonee.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shoppeclonee.viewmodel.provider.ProductViewModel

enum class ProductMode {
    ADD, EDIT, DETAIL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanProductForm(
    navController: NavHostController,
    mode: ProductMode,
    productId: Int? = null,
    vm: ProductViewModel = viewModel()
) {

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val isLoading = vm.loading.value
    val error = vm.message.value
    val product = vm.product.value

    // ================= LOAD PRODUCT =================
    LaunchedEffect(productId) {
        if (mode != ProductMode.ADD && productId != null) {
            vm.getProductById(productId)
        }
    }

    // ================= SET DATA =================
    LaunchedEffect(product) {
        product?.let {
            name = it.name
            price = it.price.toString()
            stock = it.stock.toString()
            description = it.description ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (mode) {
                            ProductMode.ADD -> "Tambah Produk"
                            ProductMode.EDIT -> "Edit Produk"
                            ProductMode.DETAIL -> "Detail Produk"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Produk") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Harga") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stok") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            // ================= ERROR =================
            if (!error.isNullOrBlank()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }



            Spacer(Modifier.height(16.dp))

            when (mode) {

                ProductMode.ADD -> {
                    Button(
                        onClick = {
                            vm.addProduct(
                                name = name,
                                price = price.toIntOrNull() ?: 0,
                                stock = stock.toIntOrNull() ?: 0,
                                description = description
                            ) {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "Menyimpan..." else "Simpan")
                    }
                }

                ProductMode.EDIT -> {
                    Button(
                        onClick = {
                            vm.updateProduct(
                                id = productId!!,
                                name = name,
                                price = price.toIntOrNull() ?: 0,
                                stock = stock.toIntOrNull() ?: 0,
                                description = description
                            ) {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "Mengupdate..." else "Update")
                    }
                }

                ProductMode.DETAIL -> {
                    Button(
                        onClick = {
                            navController.navigate("product_edit/$productId")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Edit Produk")
                    }
                }
            }
        }
    }
}

