package com.example.shoppeclonee.viewmodel.provider

import android.R.attr.id
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.shoppeclonee.modeldata.User
import com.example.shoppeclonee.repositori.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    var user = mutableStateOf<User?>(null)
        private set

    var message = mutableStateOf("")
        private set

    var token = mutableStateOf<String?>(null)
        private set


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val res = repo.login(email, password)
                if (res.status) {
                    user.value = res.user
                    token.value = res.token   // 🔥 FIX
                }
                message.value = res.message
            } catch (e: Exception) {
                message.value = "Gagal login"
            }
        }
    }

    fun register(username: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            try {
                val res = repo.register(username, email, password, role)
                message.value = res.message
            } catch (e: Exception) {
                message.value = "Gagal register"
            }
        }
    }

    fun logout() {
        user.value = null
    }
}
