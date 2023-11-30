package com.example.moneyminder

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.Manifest
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    val  CONTACTS_PERMISSION_CODE=123

    var rv: RecyclerView? =null;

    @SuppressLint("Range", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv = findViewById<RecyclerView>(R.id.rv)
        var search= findViewById<EditText>(R.id.search)
        var adapter = MyAdapter(LL.contacts!!, this)
        rv!!.layoutManager = LinearLayoutManager(this)
        rv!!.adapter = adapter
        search.doOnTextChanged() { text, start, before, count ->
            adapter.filter.filter(text)
        }




    }


}