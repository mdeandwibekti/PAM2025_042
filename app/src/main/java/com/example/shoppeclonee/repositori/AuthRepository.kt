package com.example.shoppeclonee.repositori

class AuthRepository(
    private val container: ContainerApp = ContainerApp.instance
) {

    suspend fun register(
        username: String,
        email: String,
        password: String,
        role: String
    ) = container.userApi.register(
        mapOf(
            "username" to username,
            "email" to email,
            "password" to password,
            "role" to role
        )
    )

    suspend fun login(email: String, password: String) =
        container.userApi.login(
            mapOf(
                "email" to email,
                "password" to password
            )
        )

    suspend fun getUser(id: Int) =
        container.userApi.getUser(id)
}


