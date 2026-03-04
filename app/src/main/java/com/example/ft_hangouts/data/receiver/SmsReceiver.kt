package com.example.ft_hangouts.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.example.ft_hangouts.data.database.ContactDatabaseHelper
import com.example.ft_hangouts.data.model.Message

class SmsReceiver : BroadcastReceiver() {
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
                    
                    // TODO: Notify the UI or show a notification
                }
            }
        }
    }
}
