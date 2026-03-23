package com.example.ft_hangouts.ui.edit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ft_hangouts.R
import com.example.ft_hangouts.data.database.ContactDatabaseHelper
import com.example.ft_hangouts.data.model.Contact
import com.example.ft_hangouts.ui.BaseActivity
import com.example.ft_hangouts.ui.landscapeLeftSafeArea
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme

private const val MAX_PHONE_DIGITS = 15

private fun sanitizePhoneInput(input: String): String {
    val hasLeadingPlus = input.firstOrNull() == '+'
    val digitsOnly = input.filter { it.isDigit() }.take(MAX_PHONE_DIGITS)
    return if (hasLeadingPlus) "+$digitsOnly" else digitsOnly
}

private fun isValidPhoneNumber(phone: String): Boolean {
    return phone.matches(Regex("^\\+?\\d{7,15}$"))
}

class ContactEditActivity : BaseActivity() {
    private lateinit var dbHelper: ContactDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = ContactDatabaseHelper(this)
        val contactId = intent.getLongExtra("CONTACT_ID", -1L)

        setContent {
            val themeVariant by themeVariantState
            Ft_hangoutsTheme(themeVariant = themeVariant) {
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

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

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
                    IconButton(onClick = {
                        nameError = name.isBlank()
                        phoneError = phone.isBlank() || !isValidPhoneNumber(phone)
                        if (!nameError && !phoneError) {
                            onSave(name, phone, email, address, notes)
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = stringResource(R.string.save))
                    }
                },
            )
        },
        modifier = Modifier.landscapeLeftSafeArea()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    if (nameError && it.isNotBlank()) nameError = false
                },
                label = { Text(stringResource(R.string.name)) },
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text(stringResource(R.string.error_name_required))
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { 
                    val sanitizedPhone = sanitizePhoneInput(it)
                    phone = sanitizedPhone
                    if (phoneError && isValidPhoneNumber(sanitizedPhone)) phoneError = false
                },
                label = { Text(stringResource(R.string.phone_number)) },
                isError = phoneError,
                supportingText = {
                    if (phoneError) {
                        Text(
                            if (phone.isBlank()) stringResource(R.string.error_phone_required)
                            else stringResource(R.string.error_phone_invalid)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
