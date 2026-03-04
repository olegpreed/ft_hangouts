package com.example.ft_hangouts.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ft_hangouts.data.model.Contact
import com.example.ft_hangouts.data.model.Message

class ContactDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 2
        
        // Contacts Table
        private const val TABLE_CONTACTS = "contacts"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_EMAIL = "email"
        private const val KEY_ADDRESS = "address"
        private const val KEY_NOTES = "notes"
        private const val KEY_PROFILE_PICTURE = "profile_picture"

        // Messages Table
        private const val TABLE_MESSAGES = "messages"
        private const val KEY_MSG_ID = "id"
        private const val KEY_MSG_CONTACT_ID = "contact_id"
        private const val KEY_MSG_TEXT = "text"
        private const val KEY_MSG_TIMESTAMP = "timestamp"
        private const val KEY_MSG_IS_SENT = "is_sent"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createContactsTable = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_NOTES + " TEXT,"
                + KEY_PROFILE_PICTURE + " TEXT"
                + ")")
        db?.execSQL(createContactsTable)

        val createMessagesTable = ("CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_MSG_ID + " INTEGER PRIMARY KEY,"
                + KEY_MSG_CONTACT_ID + " INTEGER,"
                + KEY_MSG_TEXT + " TEXT,"
                + KEY_MSG_TIMESTAMP + " INTEGER,"
                + KEY_MSG_IS_SENT + " INTEGER,"
                + "FOREIGN KEY(" + KEY_MSG_CONTACT_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + KEY_ID + ")"
                + ")")
        db?.execSQL(createMessagesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val createMessagesTable = ("CREATE TABLE " + TABLE_MESSAGES + "("
                    + KEY_MSG_ID + " INTEGER PRIMARY KEY,"
                    + KEY_MSG_CONTACT_ID + " INTEGER,"
                    + KEY_MSG_TEXT + " TEXT,"
                    + KEY_MSG_TIMESTAMP + " INTEGER,"
                    + KEY_MSG_IS_SENT + " INTEGER,"
                    + "FOREIGN KEY(" + KEY_MSG_CONTACT_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + KEY_ID + ")"
                    + ")")
            db?.execSQL(createMessagesTable)
        }
    }

    // Contact Methods
    fun addContact(contact: Contact): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, contact.name)
            put(KEY_PHONE_NUMBER, contact.phoneNumber)
            put(KEY_EMAIL, contact.email)
            put(KEY_ADDRESS, contact.address)
            put(KEY_NOTES, contact.notes)
            put(KEY_PROFILE_PICTURE, contact.profilePicture)
        }
        val id = db.insert(TABLE_CONTACTS, null, values)
        db.close()
        return id
    }

    fun getContact(id: Long): Contact? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CONTACTS, arrayOf(KEY_ID, KEY_NAME, KEY_PHONE_NUMBER, KEY_EMAIL, KEY_ADDRESS, KEY_NOTES, KEY_PROFILE_PICTURE),
            "$KEY_ID=?", arrayOf(id.toString()), null, null, null, null
        )
        return if (cursor.moveToFirst()) {
            val contact = Contact(
                cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTES)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFILE_PICTURE))
            )
            cursor.close()
            contact
        } else {
            cursor.close()
            null
        }
    }

    fun getContactByPhone(phoneNumber: String): Contact? {
        val db = this.readableDatabase
        // Simple normalization for comparison, ideally we'd do better
        val cursor = db.query(
            TABLE_CONTACTS, arrayOf(KEY_ID, KEY_NAME, KEY_PHONE_NUMBER, KEY_EMAIL, KEY_ADDRESS, KEY_NOTES, KEY_PROFILE_PICTURE),
            "$KEY_PHONE_NUMBER LIKE ?", arrayOf("%$phoneNumber%"), null, null, null, null
        )
        return if (cursor.moveToFirst()) {
            val contact = Contact(
                cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTES)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFILE_PICTURE))
            )
            cursor.close()
            contact
        } else {
            cursor.close()
            null
        }
    }

    fun getAllContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val contact = Contact(
                    cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTES)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFILE_PICTURE))
                )
                contactList.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }

    fun updateContact(contact: Contact): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, contact.name)
            put(KEY_PHONE_NUMBER, contact.phoneNumber)
            put(KEY_EMAIL, contact.email)
            put(KEY_ADDRESS, contact.address)
            put(KEY_NOTES, contact.notes)
            put(KEY_PROFILE_PICTURE, contact.profilePicture)
        }
        return db.update(TABLE_CONTACTS, values, "$KEY_ID = ?", arrayOf(contact.id.toString()))
    }

    fun deleteContact(contact: Contact) {
        val db = this.writableDatabase
        db.delete(TABLE_MESSAGES, "$KEY_MSG_CONTACT_ID = ?", arrayOf(contact.id.toString()))
        db.delete(TABLE_CONTACTS, "$KEY_ID = ?", arrayOf(contact.id.toString()))
        db.close()
    }

    // Message Methods
    fun addMessage(message: Message): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_MSG_CONTACT_ID, message.contactId)
            put(KEY_MSG_TEXT, message.text)
            put(KEY_MSG_TIMESTAMP, message.timestamp)
            put(KEY_MSG_IS_SENT, if (message.isSent) 1 else 0)
        }
        val id = db.insert(TABLE_MESSAGES, null, values)
        db.close()
        return id
    }

    fun getMessagesForContact(contactId: Long): List<Message> {
        val messageList = mutableListOf<Message>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_MESSAGES, null, "$KEY_MSG_CONTACT_ID = ?",
            arrayOf(contactId.toString()), null, null, "$KEY_MSG_TIMESTAMP ASC"
        )

        if (cursor.moveToFirst()) {
            do {
                val message = Message(
                    cursor.getLong(cursor.getColumnIndexOrThrow(KEY_MSG_ID)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(KEY_MSG_CONTACT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_MSG_TEXT)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(KEY_MSG_TIMESTAMP)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MSG_IS_SENT)) == 1
                )
                messageList.add(message)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return messageList
    }
}
