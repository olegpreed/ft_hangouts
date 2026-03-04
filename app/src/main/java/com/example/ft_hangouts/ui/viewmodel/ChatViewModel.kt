package com.example.ft_hangouts.ui.viewmodel

import android.app.Application
import android.telephony.SmsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ft_hangouts.data.database.ContactDatabaseHelper
import com.example.ft_hangouts.data.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = ContactDatabaseHelper(application)
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private var currentContactId: Long = -1

    fun loadMessages(contactId: Long) {
        currentContactId = contactId
        viewModelScope.launch {
            _messages.value = dbHelper.getMessagesForContact(contactId)
        }
    }

    fun sendMessage(phoneNumber: String, text: String) {
        if (currentContactId == -1L || text.isBlank()) return

        try {
            val smsManager = getApplication<Application>().getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(phoneNumber, null, text, null, null)

            val message = Message(
                contactId = currentContactId,
                text = text,
                timestamp = System.currentTimeMillis(),
                isSent = true
            )
            viewModelScope.launch {
                dbHelper.addMessage(message)
                loadMessages(currentContactId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
