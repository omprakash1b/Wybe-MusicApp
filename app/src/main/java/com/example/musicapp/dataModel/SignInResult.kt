package com.example.musicapp.dataModel

data class SignInResult(
    val user : UserData?,
    val errorMessage:String?
)

data class UserData(
    val userId:String,
    val userName:String?,
    val profilePictureUrl:String?
)