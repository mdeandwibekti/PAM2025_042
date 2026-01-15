package com.example.shoppeclonee.repositori

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.shoppeclonee.viewmodel.provider.AuthViewModel

class ShoppeCloneApp : Application() {

    companion object {
        lateinit var authVM: AuthViewModel
    }

    override fun onCreate() {
        super.onCreate()
        authVM = AuthViewModel()
    }
}



