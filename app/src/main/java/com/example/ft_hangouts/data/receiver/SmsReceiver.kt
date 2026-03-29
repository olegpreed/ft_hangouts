package com.example.ft_hangouts.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.example.ft_hangouts.data.database.ContactDatabaseHelper
import com.example.ft_hangouts.data.model.Message

class SmsReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_MESSAGES_UPDATED = "com.example.ft_hangouts.ACTION_MESSAGES_UPDATED"
        const val EXTRA_CONTACT_ID = "EXTRA_CONTACT_ID"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val dbHelper = ContactDatabaseHelper(context)

            for (sms in messages) {
                val senderPhone = sms.displayOriginatingAddress
                val messageText = sms.displayMessageBody
                
                // Try to find a contact with this phone number
                val contact = dbHelper.getContactByPhone(senderPhone)
                
                if (contact != null) {
                    val newMessage = Message(
                        contactId = contact.id,
                        text = messageText,
                        timestamp = System.currentTimeMillis(),
                        isSent = false
                    )
                    dbHelper.addMessage(newMessage)

                    val updateIntent = Intent(ACTION_MESSAGES_UPDATED).apply {
                        `package` = context.packageName
                        putExtra(EXTRA_CONTACT_ID, contact.id)
                    }
                    context.sendBroadcast(updateIntent)
                }
            }
        }
    }
}
