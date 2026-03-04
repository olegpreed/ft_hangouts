package com.example.ft_hangouts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ft_hangouts.data.model.Contact
import com.example.ft_hangouts.ui.detail.ContactDetailActivity
import com.example.ft_hangouts.ui.edit.ContactEditActivity
import com.example.ft_hangouts.ui.settings.SettingsActivity
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme
import com.example.ft_hangouts.ui.viewmodel.ContactViewModel

class MainActivity : ComponentActivity() {
    private lateinit var contactViewModel: ContactViewModel
    private var primaryColorState = mutableStateOf(Color(0xFF6650a4))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        updateThemeColor()
        
        setContent {
            val primaryColor by primaryColorState
            contactViewModel = viewModel()

            Ft_hangoutsTheme(primary = primaryColor) {
                ContactListScreen(
                    contactViewModel = contactViewModel,
                    onAddContact = {
                        val intent = Intent(this, ContactEditActivity::class.java)
                        startActivity(intent)
                    },
                    onContactClick = { contact ->
                        val intent = Intent(this, ContactDetailActivity::class.java)
                        intent.putExtra("CONTACT_ID", contact.id)
                        startActivity(intent)
                    },
                    onSettingsClick = {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    private fun updateThemeColor() {
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedColorArgb = prefs.getInt("primary_color", Color(0xFF6650a4).toArgb())
        primaryColorState.value = Color(savedColorArgb)
    }

    override fun onResume() {
        super.onResume()
        updateThemeColor()
        if (::contactViewModel.isInitialized) {
            contactViewModel.loadContacts()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    contactViewModel: ContactViewModel = viewModel(),
    onAddContact: () -> Unit,
    onContactClick: (Contact) -> Unit,
    onSettingsClick: () -> Unit
) {
    val contacts by contactViewModel.contacts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddContact) {
                Icon(Icons.Default.Add, contentDescription = "Add Contact")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ContactList(
            contacts = contacts,
            onContactClick = onContactClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ContactList(
    contacts: List<Contact>,
    onContactClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(contacts) { contact ->
            ContactListItem(contact = contact, onClick = { onContactClick(contact) })
        }
    }
}

@Composable
fun ContactListItem(contact: Contact, onClick: () -> Unit) {
    Box(modifier = Modifier.clickable { onClick() }) {
        ListItem(
            headlineContent = { Text(contact.name) },
            supportingContent = { Text(contact.phoneNumber) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListScreenPreview() {
    Ft_hangoutsTheme {
        ContactListScreen(onAddContact = {}, onContactClick = {}, onSettingsClick = {})
    }
}
