package com.example.ft_hangouts.ui.detail

import android.content.res.Configuration
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft_hangouts.R
import com.example.ft_hangouts.data.database.ContactDatabaseHelper
import com.example.ft_hangouts.data.model.Contact
import com.example.ft_hangouts.ui.BaseActivity
import com.example.ft_hangouts.ui.chat.ChatActivity
import com.example.ft_hangouts.ui.edit.ContactEditActivity
import com.example.ft_hangouts.ui.landscapeLeftSafeArea
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme

class ContactDetailActivity : BaseActivity() {
    private lateinit var dbHelper: ContactDatabaseHelper
    private var refreshTrigger = mutableStateOf(0)
    private var hasChanges = false

    companion object {
        private const val REQUEST_EDIT = 2001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = ContactDatabaseHelper(this)
        val contactId = intent.getLongExtra("CONTACT_ID", -1L)

        setContent {
            val themeVariant by themeVariantState
            Ft_hangoutsTheme(themeVariant = themeVariant) {
                var contact by remember { mutableStateOf<Contact?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                val trigger by refreshTrigger

                LaunchedEffect(trigger) {
                    contact = dbHelper.getContact(contactId)
                    isLoading = false
                    if (contact == null && trigger > 0) {
                        Toast.makeText(this@ContactDetailActivity, getString(R.string.contact_not_found), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .landscapeLeftSafeArea(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (contact != null) {
                    ContactDetailScreen(
                        contact = contact!!,
                        onBack = { finish() },
                        onEdit = {
                            val intent = Intent(this@ContactDetailActivity, ContactEditActivity::class.java)
                            intent.putExtra("CONTACT_ID", contact!!.id)
                            startActivityForResult(intent, REQUEST_EDIT)
                        },
                        onDelete = {
                            dbHelper.deleteContact(contact!!)
                            hasChanges = true
                            finish()
                        },
                        onMessage = {
                            val intent = Intent(this@ContactDetailActivity, ChatActivity::class.java)
                            intent.putExtra("CONTACT_ID", contact!!.id)
                            intent.putExtra("CONTACT_NAME", contact!!.name)
                            intent.putExtra("CONTACT_PHONE", contact!!.phoneNumber)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshTrigger.value++
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            hasChanges = true
            refreshTrigger.value++
        }
    }

    override fun finish() {
        setResult(if (hasChanges) RESULT_OK else RESULT_CANCELED)
        super.finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    contact: Contact,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMessage: () -> Unit
) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.contact_details)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_contact))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                    }
                },
            )
        },
        bottomBar = {
            Surface() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (isPortrait) Modifier.navigationBarsPadding() else Modifier)
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onMessage,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.message))
                    }
                }
            }
        },
        modifier = Modifier.landscapeLeftSafeArea()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DetailItem(icon = Icons.Default.Phone, text = contact.phoneNumber, label = stringResource(R.string.phone_number))
            DetailItem(icon = Icons.Default.Email, text = contact.email, label = stringResource(R.string.email))
            DetailItem(icon = Icons.Default.LocationOn, text = contact.address, label = stringResource(R.string.address))

            if (contact.notes.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = stringResource(R.string.notes), style = MaterialTheme.typography.labelLarge)
                        Text(text = contact.notes, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, text: String, label: String) {
    if (text.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(24.dp))
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
