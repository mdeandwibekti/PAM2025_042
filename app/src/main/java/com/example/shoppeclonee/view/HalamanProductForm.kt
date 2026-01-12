package com.example.shoppeclonee.view

import androidx.compose.foundation.layout.*
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

    // 🔥 LOAD DATA UNTUK EDIT / DETAIL
    LaunchedEffect(productId) {
        if (mode != ProductMode.ADD && productId != null) {
            vm.getProductById(productId)
        }
    }

    // 🔥 SET DATA SAAT DETAIL / EDIT
    vm.product.value?.let {
        name = it.name
        price = it.price.toString()
        stock = it.stock.toString()
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
                }
            )
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { if (mode != ProductMode.DETAIL) name = it },
                label = { Text("Nama Produk") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            OutlinedTextField(
                value = price,
                onValueChange = { if (mode != ProductMode.DETAIL) price = it },
                label = { Text("Harga") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { if (mode != ProductMode.DETAIL) stock = it },
                label = { Text("Stok") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            Spacer(Modifier.height(16.dp))

            // 🔥 ACTION BUTTON
            when (mode) {
                ProductMode.ADD -> {
                    Button(
                        onClick = {
                            vm.addProduct(
                                mapOf(
                                    "name" to name,
                                    "price" to price.toInt(),
                                    "stock" to stock.toInt()
                                )
                            )
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simpan")
                    }
                }

                ProductMode.EDIT -> {
                    Button(
                        onClick = {
                            vm.updateProduct(
                                id = productId!!,
                                body = mapOf(
                                    "name" to name,
                                    "price" to price.toInt(),
                                    "stock" to stock.toInt()
                                )
                            )
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update")
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
