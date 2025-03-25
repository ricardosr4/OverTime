package com.example.overtime.data.model

data class UserModel(
    val userId: String,
    val email: String,
    //se podria agregar un user name
){
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "userId" to this.userId,
            "email" to this.email,

        )
    }
}
