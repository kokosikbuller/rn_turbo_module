package com.nativelocalstorage

import android.content.Context
import android.content.SharedPreferences
import com.nativelocalstorage.NativeContactsSpec
import com.facebook.react.bridge.ReactApplicationContext

import android.provider.ContactsContract
import org.json.JSONArray
import org.json.JSONObject

class NativeContactsModule(reactContext: ReactApplicationContext) : NativeContactsSpec(reactContext) {

  override fun getName() = NAME

  override fun getContactList(): String {
    val contactsArray = JSONArray()
    val contentResolver = reactApplicationContext.contentResolver
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )

    cursor?.use {
        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (cursor.moveToNext()) {
            val contactObj = JSONObject()
            contactObj.put("name", cursor.getString(nameIndex))
            contactObj.put("phone", cursor.getString(numberIndex))
            contactsArray.put(contactObj)
        }
    }

    return contactsArray.toString()
  }

  companion object {
    const val NAME = "NativeContacts"
  }
}