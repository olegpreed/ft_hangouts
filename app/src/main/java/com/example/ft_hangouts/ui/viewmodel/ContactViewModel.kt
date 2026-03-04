package com.example.ft_hangouts.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ft_hangouts.data.database.ContactDatabaseHelper
import com.example.ft_hangouts.data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = ContactDatabaseHelper(application)

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        loadContacts()
    }

    fun loadContacts() {
        viewModelScope.launch {
            _contacts.value = dbHelper.getAllContacts()
        }
    }

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            dbHelper.addContact(contact)
            loadContacts()
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            dbHelper.updateContact(contact)
            loadContacts()
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            dbHelper.deleteContact(contact)
            loadContacts()
        }
    }
}
