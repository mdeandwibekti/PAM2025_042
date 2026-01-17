package com.example.shoppeclonee.uicontroller.route

import HalamanLogin
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shoppeclonee.view.*
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel
import com.example.shoppeclonee.viewmodel.provider.CartViewModel

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
        composable(DestinasiHome.route) {
            HalamanHome(
                navController = navController,
                authVM = authVM,
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

        composable("product_add") {

            if (authVM.token.value.isNullOrEmpty()) {
                navController.navigate("login")
            } else {
                HalamanProductForm(
                    navController = navController,
                    mode = ProductMode.ADD,
                    authVM = authVM
                )
            }
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

        composable(
            route = "product_edit/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            if (authVM.token.value.isNullOrEmpty()) {
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

        /* ================= CART ================= */
        composable(DestinasiCart.route) {

            val cartVM: CartViewModel = viewModel()

            HalamanCart(
                navController = navController,
                authVM = authVM,
                cartVM = cartVM
            )
        }

        /* ================= CHECKOUT ================= */
        composable(DestinasiCheckout.route) {

            val token = authVM.token.value ?: ""

            HalamanCheckout(
                token = token,
                onOrderCreated = {
                    navController.navigate(DestinasiOrder.route) {
                        popUpTo(DestinasiCart.route) { inclusive = true }
                    }
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
