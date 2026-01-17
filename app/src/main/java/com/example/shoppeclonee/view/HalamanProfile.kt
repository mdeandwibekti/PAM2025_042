package com.example.shoppeclonee.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppeclonee.uicontroller.route.DestinasiHome
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel

@Composable
fun HalamanProfile(
    navController: NavHostController,
    authVM: AuthViewModel,
    onLoginClick: () -> Unit
) {
    val user = authVM.user.value

    Scaffold(
        topBar = { TopAppBarLokalku("Profil") },
        bottomBar = { BottomBarLokalku(navController) }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
        ) {

            /* ================= BELUM LOGIN ================= */
            if (user == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(12.dp))
                    Text(
                        "LOKALKU",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text("Belum login", style = MaterialTheme.typography.bodyMedium)

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text("Login")
                    }
                }
            }

            /* ================= SUDAH LOGIN ================= */
            else {

                /* ---------- HEADER PROFILE ---------- */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "LOKALKU",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(16.dp))

                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(72.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                user.username.first().uppercase(),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        user.username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        user.email ?: "-",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        "Role: ${user.role}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(Modifier.height(24.dp))

                /* ================= MENU SELLER ================= */
                if (user.role == "seller") {
                    ListItem(
                        headlineContent = { Text("Dashboard Seller") },
                        leadingContent = {
                            Icon(Icons.Default.Store, contentDescription = null)
                        },
                        modifier = Modifier.clickable {
                            navController.navigate("seller_home")
                        }
                    )
                    Divider()
                }

                /* ================= LOGOUT ================= */
                ListItem(
                    headlineContent = {
                        Text(
                            "Logout",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    modifier = Modifier.clickable {
                        authVM.logout()
                        navController.navigate(DestinasiHome.route) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}

