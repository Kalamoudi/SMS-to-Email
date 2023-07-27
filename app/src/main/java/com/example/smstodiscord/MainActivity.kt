package com.example.smstodiscord

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Java imports
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

// Kotlin imports
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(){

    private val SMS_PERMISSION_REQUEST_CODE = 101
    private lateinit var smsRecyclerView: RecyclerView
    private lateinit var checkPermissions: CheckPermissions
    private lateinit var handleSMS: HandleSMS
    private lateinit var googleSignIn: GoogleSignIn

    // Google Sign-in variables
    //private lateinit var googleSignInClient: GoogleSignInClient
    //private val RC_SIGN_IN = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)

        // Checks for permissions
        checkPermissions = CheckPermissions()
        checkPermissions.checkSmsPermission(this)

        handleSMS = HandleSMS()

        handleSMS.handleSMS(this)


        //Code for google sign-ins, currently in testing phase
//        googleSignIn = GoogleSignIn()
          //googleSignIn.handleGoogleSignIn()
//
//

    }


}

