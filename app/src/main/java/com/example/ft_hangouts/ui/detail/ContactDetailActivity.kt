package com.example.ft_hangouts.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.ft_hangouts.data.database.ContactDatabaseHelper
import com.example.ft_hangouts.data.model.Contact
import com.example.ft_hangouts.ui.edit.ContactEditActivity
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme

class ContactDetailActivity : ComponentActivity() {
    private lateinit var dbHelper: ContactDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = ContactDatabaseHelper(this)
        val contactId = intent.getLongExtra("CONTACT_ID", -1L)

        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedColorArgb = prefs.getInt("primary_color", Color(0xFF6650a4).toArgb())

        setContent {
            Ft_hangoutsTheme(primary = Color(savedColorArgb)) {
                var contact by remember { mutableStateOf<Contact?>(null) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    contact = dbHelper.getContact(contactId)
                    isLoading = false
                    if (contact == null) {
                        Toast.makeText(this@ContactDetailActivity, "Contact not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (contact != null) {
                    ContactDetailScreen(
                        contact = contact!!,
                        onBack = { finish() },
                        onEdit = {
                            val intent = Intent(this@ContactDetailActivity, ContactEditActivity::class.java)
                            intent.putExtra("CONTACT_ID", contact!!.id)
                            startActivity(intent)
                        },
                        onDelete = {
                            dbHelper.deleteContact(contact!!)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    contact: Contact,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DetailItem(icon = Icons.Default.Phone, text = contact.phoneNumber, label = "Phone")
            DetailItem(icon = Icons.Default.Email, text = contact.email, label = "Email")
            DetailItem(icon = Icons.Default.LocationOn, text = contact.address, label = "Address")

            if (contact.notes.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Notes", style = MaterialTheme.typography.labelLarge)
                        Text(text = contact.notes, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { /* TODO: SMS */ },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Message")
                }
                Button(
                    onClick = { /* TODO: Call */ },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Call")
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
