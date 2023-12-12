package com.example.moneyminder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    var mAuth=FirebaseAuth.getInstance()
    val  CONTACTS_PERMISSION_CODE=123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (ContextCompat.checkSelfPermission(
                this,Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        LL.contacts=MyApp().readContacts(contentResolver)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        if (mAuth.currentUser==null) {
                            var intent = Intent(this@Splash, SignIn::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }else{
                            var intent = Intent(this@Splash, Landing::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            }
            thread.start()


        } else {
            requestContactsPermission()
        }


    }
    private fun requestContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                CONTACTS_PERMISSION_CODE
            )
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CONTACTS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with your logic
                    MyApp().readContacts(contentResolver)
                } else {
                    // Permission denied, handle accordingly (e.g., show a message)
                }
            }
        }
    }
}