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

    var token = mutableStateOf<String?>(null)
        private set

    var user = mutableStateOf<User?>(null)
        private set

    var message = mutableStateOf<String?>(null)
        private set

    var role = mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repo.login(email, password)
                if (response.status) {
                    user.value = response.user
                    token.value = response.token
                    role.value = response.user?.role
                    message.value = null
                } else {
                    message.value = response.message
                }
            } catch (e: Exception) {
                message.value = e.message ?: "Terjadi kesalahan"
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
        token.value = null
        role.value = null
        message.value = null
    }
}
