package com.example.ft_hangouts

import android.Manifest
import android.content.pm.PackageManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ft_hangouts.data.model.Contact
import com.example.ft_hangouts.ui.BaseActivity
import com.example.ft_hangouts.ui.detail.ContactDetailActivity
import com.example.ft_hangouts.ui.edit.ContactEditActivity
import com.example.ft_hangouts.ui.landscapeHorizontalSafeArea
import com.example.ft_hangouts.ui.settings.SettingsActivity
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme
import com.example.ft_hangouts.ui.viewmodel.ContactViewModel

class MainActivity : BaseActivity() {
    private lateinit var contactViewModel: ContactViewModel
    private val smsPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestSmsPermissionsIfNeeded()

        setContent {
            val themeVariant by themeVariantState
            contactViewModel = viewModel()

            Ft_hangoutsTheme(themeVariant = themeVariant) {
                ContactListScreen(
                    contactViewModel = contactViewModel,
                    onAddContact = {
                        val intent = Intent(this, ContactEditActivity::class.java)
                        startActivityForResult(intent, REQUEST_EDIT)
                    },
                    onContactClick = { contact ->
                        val intent = Intent(this, ContactDetailActivity::class.java)
                        intent.putExtra("CONTACT_ID", contact.id)
                        startActivityForResult(intent, REQUEST_DETAIL)
                    },
                    onSettingsClick = {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    private fun requestSmsPermissionsIfNeeded() {
        val missingPermissions = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        ).filter { permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            smsPermissionsLauncher.launch(missingPermissions.toTypedArray())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == REQUEST_EDIT || requestCode == REQUEST_DETAIL) && resultCode == RESULT_OK) {
            if (::contactViewModel.isInitialized) {
                contactViewModel.loadContacts()
            }
        }
    }

    companion object {
        private const val REQUEST_EDIT = 1001
        private const val REQUEST_DETAIL = 1002
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
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val filteredContacts = contacts.filter {
        it.name.contains(query, ignoreCase = true) || it.phoneNumber.contains(query)
    }

    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .semantics { isTraversalGroup = true }
                    .padding(horizontal = if (active) 0.dp else 16.dp, vertical = if (active) 0.dp else 8.dp)
            ) {
                SearchBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .semantics { traversalIndex = 0f }
                        .fillMaxWidth(),
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = query,
                            onQueryChange = { query = it },
                            onSearch = { active = false },
                            expanded = active,
                            onExpandedChange = { active = it },
                            placeholder = { Text(stringResource(R.string.search_contacts)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                Row {
                                    if (query.isNotEmpty()) {
                                        IconButton(onClick = { query = "" }) {
                                            Icon(Icons.Default.Clear, contentDescription = null)
                                        }
                                    }
                                    IconButton(onClick = onSettingsClick) {
                                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                                    }
                                }
                            },
                        )
                    },
                    expanded = active,
                    onExpandedChange = { active = it },
                ) {
                    ContactList(
                        contacts = filteredContacts,
                        onContactClick = onContactClick
                    )
                }
            }
        },
        floatingActionButton = {
            if (!active) {
                FloatingActionButton(onClick = onAddContact) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_contact))
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .landscapeHorizontalSafeArea()
    ) { innerPadding ->
        if (!active) {
            ContactList(
                contacts = filteredContacts,
                onContactClick = onContactClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
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
