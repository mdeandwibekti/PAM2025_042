package com.example.shoppeclonee.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppeclonee.uicontroller.route.DestinasiProfile
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanSellerHome(
    navController: NavHostController,
    authVM: AuthViewModel // ⬅️ DIKIRIM DARI NAVIGASI
) {

    val token = authVM.token.value
    val user = authVM.user.value

    // ================= PROTEKSI SELLER =================
    if (token.isNullOrEmpty() || user == null || user.role != "seller") {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo(0)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard Seller") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(DestinasiProfile.route) {
                                popUpTo("seller_home") { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Kembali ke Profil"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBarLokalku(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("product_add") }
            ) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Selamat datang 👋 ${user.username}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Kelola produk UMKM Anda dengan mudah",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "📦 Produk",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Tambah, edit, dan hapus produk Anda",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Text(
                text = "Gunakan tombol + untuk menambahkan produk baru",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
