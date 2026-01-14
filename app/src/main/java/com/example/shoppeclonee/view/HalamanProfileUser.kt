package com.example.shoppeclonee.view

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanProfileUser(
    navController: NavHostController,
    authVM: AuthViewModel = viewModel()
) {

    val user = authVM.user.value

    // 🔐 JIKA BELUM LOGIN
    if (user == null) {
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
                title = { Text("Profil Saya") }
            )
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ===== USER INFO =====
            Text(
                text = user.username,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(4.dp))

            user.email?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(16.dp))

            Divider()

            Spacer(Modifier.height(16.dp))

            // ===== MENU USER =====
            Button(
                onClick = {
                    navController.navigate("order")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pesanan Saya")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate("notifikasi")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Notifikasi")
            }

            Spacer(Modifier.height(24.dp))

            // ===== LOGOUT =====
            Button(
                onClick = {
                    authVM.logout()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout")
            }
        }
    }
}
