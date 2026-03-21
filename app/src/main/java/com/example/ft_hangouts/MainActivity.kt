package com.example.ft_hangouts

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ft_hangouts.data.model.Contact
import com.example.ft_hangouts.ui.BaseActivity
import com.example.ft_hangouts.ui.detail.ContactDetailActivity
import com.example.ft_hangouts.ui.edit.ContactEditActivity
import com.example.ft_hangouts.ui.landscapeLeftSafeArea
import com.example.ft_hangouts.ui.settings.SettingsActivity
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme
import com.example.ft_hangouts.ui.viewmodel.ContactViewModel

class MainActivity : BaseActivity() {
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.contacts)) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
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
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_contact))
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .landscapeLeftSafeArea()
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
