package com.example.shoppeclonee.uicontroller.route

import HalamanLogin
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shoppeclonee.view.*
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel

@Composable
fun PetaNavigasi(
    navController: NavHostController,
    authVM: AuthViewModel
) {

    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route
    ) {

        /* ================= HOME ================= */
        // D:/semester 5/PAM/shoppeclonee/app/src/main/java/com/example/shoppeclonee/uicontroller/route/PetaNavigasi.kt

        composable(DestinasiHome.route) {
            HalamanHome(
                navController = navController,
                authVM = authVM, // Kirim authVM di sini
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                }
            )
        }


        /* ================= LOGIN ================= */
        composable("login") {
            HalamanLogin(
                vm = authVM,
                onLoginSuccess = { role ->
                    if (role == "seller") {
                        navController.navigate("seller_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate(DestinasiHome.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        /* ================= REGISTER ================= */
        composable("register") {
            HalamanRegister(
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= SELLER HOME ================= */
        composable("seller_home") {

            val user = authVM.user.value

            // 🔐 GUARD SELLER
            if (user == null || user.role != "seller") {
                navController.navigate("login") {
                    popUpTo(0)
                }
            } else {
                HalamanSellerHome(
                    navController = navController,
                    authVM = authVM
                )
            }
        }

        /* ================= PROFILE ================= */
        composable(DestinasiProfile.route) {
            HalamanProfile(
                navController = navController,
                authVM = authVM,
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        /* ================= PRODUCT DETAIL ================= */
        composable(
            route = "product_detail/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            HalamanProductForm(
                navController = navController,
                mode = ProductMode.DETAIL,
                productId = id,
                authVM = authVM
            )
        }

        /* ================= PRODUCT ADD ================= */
        composable("product_add") {

            // 🔐 GUARD LOGIN
            if (authVM.token.value.isNullOrBlank()) {
                navController.navigate("login")
            } else {
                HalamanProductForm(
                    navController = navController,
                    mode = ProductMode.ADD,
                    authVM = authVM
                )
            }
        }

        /* ================= PRODUCT EDIT ================= */
        composable(
            route = "product_edit/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            if (authVM.token.value.isNullOrBlank()) {
                navController.navigate("login")
            } else {
                HalamanProductForm(
                    navController = navController,
                    mode = ProductMode.EDIT,
                    productId = id,
                    authVM = authVM
                )
            }
        }

        composable("seller_products") {
            HalamanSellerProductList(
                navController = navController,
                authVM = authVM
            )
        }


        /* ================= CART ================= */
        composable(DestinasiCart.route) {
            HalamanCart(
                onCheckout = {
                    navController.navigate(DestinasiOrder.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        /* ================= ORDER ================= */
        composable(DestinasiOrder.route) {
            HalamanOrder(
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= NOTIFICATION ================= */
        composable(DestinasiNotifikasi.route) {
            HalamanNotifikasi(
                navController = navController
            )
        }
    }
}
