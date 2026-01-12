package com.example.shoppeclonee.uicontroller.route

import HalamanLogin
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.shoppeclonee.view.*
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel

@Composable
fun PetaNavigasi(navController: NavHostController,
                    authVM: AuthViewModel
) {

    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route
    ) {

        // ================= HOME =================
        composable(DestinasiHome.route) {
            HalamanHome(
                navController = navController,
                onProductClick = { productId ->
                    navController.navigate(
                        DestinasiProductDetail.createRoute(productId)
                    )
                }
            )
        }

        composable("login") {
            HalamanLogin(
                onLoginSuccess = { role ->
                    if (role == "seller") {
                        navController.navigate("seller_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("user_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("user_home") {
            HalamanHome(
                navController = navController,
                onProductClick = { id ->
                    navController.navigate("product_detail/$id")
                }
            )
        }

        composable("seller_home") {
            HalamanSellerHome(
                navController = navController
            )
        }


        composable("register") {
            HalamanRegister(
                onBack = { navController.popBackStack() }
            )
        }



        // ============== PRODUCT DETAIL ============


        // ================= CART =================
        composable(DestinasiCart.route) {
            HalamanCart(
                onCheckout = {
                    navController.navigate(DestinasiOrder.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ================= ORDERS =================
        composable(DestinasiOrder.route) {
            HalamanOrders(
                onBack = { navController.popBackStack() }
            )
        }

        // ================= SELLER =================



        // Entry input produk (opsional sementara)


        composable("checkout") {
            HalamanCheckout(
                onOrderCreated = {
                    navController.navigate(DestinasiOrder.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(DestinasiNotifikasi.route) {
            HalamanNotifikasi(
                navController = navController
            )
        }

        composable(DestinasiProfile.route) {
            HalamanProfile(
                navController = navController,
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("product_add") {
            HalamanProductForm(
                navController = navController,
                mode = ProductMode.ADD,
            )
        }

        composable("product_edit/{id}") {
            val id = it.arguments?.getString("id")!!.toInt()
            HalamanProductForm(
                navController = navController,
                mode = ProductMode.EDIT,
                productId = id,
            )
        }

        composable("product_detail/{id}") {
            val id = it.arguments?.getString("id")!!.toInt()
            HalamanProductForm(
                navController = navController,
                mode = ProductMode.DETAIL,
                productId = id,
            )
        }


    }
}
