package com.example.moneyminder

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log

class MyApp : Application()
{

    override fun onCreate() {
        super.onCreate()

    }
    @SuppressLint("Range")
    public fun readContacts(cr:ContentResolver): ArrayList<Contact> {
        var ls = ArrayList<Contact>();
        var adapter: MyAdapter? = null
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cur!!.count > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                    while (pCur!!.moveToNext()) {
                        val phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val id = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
//                        Log.e("name", name)
                        ls.add(Contact(name, phoneNo, id))

                    }
                    pCur.close()
                }
            }
        }

        return ls;
    }
}