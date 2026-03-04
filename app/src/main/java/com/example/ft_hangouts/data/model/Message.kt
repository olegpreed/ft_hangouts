package com.example.ft_hangouts.data.model

data class Message(
    val id: Long = 0,
    val contactId: Long,
    val text: String,
    val timestamp: Long,
    val isSent: Boolean // true if sent by the user, false if received
)
