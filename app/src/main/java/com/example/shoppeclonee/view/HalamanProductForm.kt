package com.example.shoppeclonee.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.shoppeclonee.repositori.ProductRepository
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel
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
    authVM: AuthViewModel
) {
    val vm: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(authVM)
    )

    // State Input
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") } // State untuk foto

    val isLoading = vm.loading.value
    val error = vm.message.value
    val product = vm.product.value

    // Launcher untuk mengambil gambar dari galeri HP
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { imageUrl = it.toString() }
    }

    // Load data jika mode EDIT atau DETAIL
    LaunchedEffect(productId) {
        if (mode != ProductMode.ADD && productId != null) {
            vm.getProductById(productId)
        }
    }

    // Sinkronisasi data dari ViewModel ke State lokal
    LaunchedEffect(product) {
        product?.let {
            name = it.name
            price = it.price.toString()
            stock = it.stock.toString()
            description = it.description ?: ""
            imageUrl = it.image ?: "" // Mengambil URL foto yang sudah ada
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (mode) {
                            ProductMode.ADD -> "Tambah Produk Baru"
                            ProductMode.EDIT -> "Ubah Produk"
                            ProductMode.DETAIL -> "Informasi Produk"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // --- BAGIAN FOTO PRODUK ---
            Text(text = "Foto Produk", style = MaterialTheme.typography.titleMedium)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable(enabled = mode != ProductMode.DETAIL) {
                        launcher.launch("image/*")
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    if (imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Preview Gambar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Belum ada foto")
                            if(mode != ProductMode.DETAIL) Text("(Klik untuk pilih)", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // --- INPUT FIELDS ---
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
                label = { Text("Harga (Rp)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Jumlah Stok") },
                modifier = Modifier.fillMaxWidth(),
                enabled = mode != ProductMode.DETAIL
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi Singkat") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                enabled = mode != ProductMode.DETAIL
            )

            if (!error.isNullOrBlank()) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            // --- TOMBOL AKSI ---
            when (mode) {
                ProductMode.ADD -> {
                    Button(
                        onClick = {
                            vm.addProduct(
                                name = name,
                                price = price.toIntOrNull() ?: 0,
                                stock = stock.toIntOrNull() ?: 0,
                                description = description,
                                image = imageUrl // Mengirim URI gambar
                            ) {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && name.isNotEmpty()
                    ) {
                        Text(if (isLoading) "Proses Menyimpan..." else "Simpan Produk")
                    }
                }

                ProductMode.EDIT -> {
                    Button(
                        onClick = {
                            vm.updateProduct(
                                id = productId ?: return@Button,
                                name = name,
                                price = price.toIntOrNull() ?: 0,
                                stock = stock.toIntOrNull() ?: 0,
                                description = description,
                                image = imageUrl // Mengirim URI gambar baru/lama
                            ) {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "Sedang Update..." else "Update Data")
                    }
                }

                ProductMode.DETAIL -> {
                    Button(
                        onClick = {
                            navController.navigate("product_edit/$productId")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Buka Mode Edit")
                    }
                }
            }
        }
    }
}

class ProductViewModelFactory(
    private val authVM: AuthViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(
                repo = ProductRepository(),
                authVM = authVM
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}