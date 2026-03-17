package com.example.ft_hangouts.ui.edit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft_hangouts.R
import com.example.ft_hangouts.data.database.ContactDatabaseHelper
import com.example.ft_hangouts.data.model.Contact
import com.example.ft_hangouts.ui.BaseActivity
import com.example.ft_hangouts.ui.landscapeLeftSafeArea
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme

class ContactEditActivity : BaseActivity() {
    private lateinit var dbHelper: ContactDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = ContactDatabaseHelper(this)
        val contactId = intent.getLongExtra("CONTACT_ID", -1L)

        setContent {
            val primaryColor by primaryColorState
            Ft_hangoutsTheme(primary = primaryColor) {
                var contact by remember { mutableStateOf<Contact?>(null) }

                LaunchedEffect(Unit) {
                    if (contactId != -1L) {
                        contact = dbHelper.getContact(contactId)
                    }
                }

                ContactEditScreen(
                    contact = contact,
                    onSave = { name, phone, email, address, notes ->
                        val updatedContact = Contact(
                            id = contact?.id ?: 0,
                            name = name,
                            phoneNumber = phone,
                            email = email,
                            address = address,
                            notes = notes,
                            profilePicture = contact?.profilePicture ?: ""
                        )
                        if (contactId == -1L) {
                            dbHelper.addContact(updatedContact)
                        } else {
                            dbHelper.updateContact(updatedContact)
                        }
                        setResult(RESULT_OK)
                        finish()
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactEditScreen(
    contact: Contact?,
    onSave: (String, String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(contact) {
        contact?.let {
            name = it.name
            phone = it.phoneNumber
            email = it.email
            address = it.address
            notes = it.notes
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (contact == null) stringResource(R.string.add_contact) else stringResource(R.string.edit_contact)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { onSave(name, phone, email, address, notes) }) {
                        Icon(Icons.Default.Check, contentDescription = stringResource(R.string.save))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = Modifier.landscapeLeftSafeArea()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(stringResource(R.string.phone_number)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.address)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(stringResource(R.string.notes)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                minLines = 3
            )
        }
    }
}
