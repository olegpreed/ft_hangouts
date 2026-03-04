package com.example.ft_hangouts.data.model

data class Contact(
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
    val notes: String,
    val profilePicture: String
)
