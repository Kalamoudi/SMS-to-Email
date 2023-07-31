package com.example.smstoemail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.smstoemail.Pages.HandleMainPageViews
import com.example.smstoemail.Permissions.CheckPermissions
import com.example.smstoemail.Services.BackgroundService
import com.example.smstoemail.Sms.HandleSMS
import com.example.smstoemail.Utils



// Kotlin imports

open class MainActivity : AppCompatActivity(){


    private val permissionsList = Utils.permissionsList
    private lateinit var checkPermissions: CheckPermissions
    private lateinit var handleSMS: HandleSMS
    private lateinit var handleMainPageView: HandleMainPageViews
    private lateinit var drawerLayout : DrawerLayout
    lateinit var serviceIntent: Intent




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)






        serviceIntent = Intent(this, BackgroundService::class.java)
        startService(serviceIntent)


        // Handles drawer functionality
        drawerLayout = findViewById(R.id.drawerLayout)
        val openSlidingButton = findViewById<Button>(R.id.openSlidingButton)

        openSlidingButton.setOnClickListener {
            // Open the sliding window
            drawerLayout.openDrawer(findViewById(R.id.slidingWindowLayout))
        }




        // HandlesSMS Receive and sending of Email
        handleSMS = HandleSMS()
        handleSMS.handleSMS(this)

        // Handling the main page views
        handleMainPageView = HandleMainPageViews()
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("app_force_stopped", true)) {
            HandleMainPageViews.setForcedStopped(false)
            val editor = sharedPreferences.edit()
            editor.remove("app_force_stopped")
            editor.apply()
        }
        handleMainPageView.handleViews(this)

//        serviceIntent = Intent(this, BackgroundService::class.java)
//        stopService(serviceIntent)


        // Checks for permissions
        checkPermissions = CheckPermissions()
        checkPermissions.handlePermissions(this)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissionsList: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults)
        checkPermissions.handlePermissionsResult(this, requestCode, permissionsList, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.PERMISSION_SETTINGS_REQUEST_CODE) {
            // Check if the user has granted the permission after navigating to settings
            if (checkPermissions.allPermissionsGranted(this)) {
                // Permission has been granted, do the necessary action here
            } else {
                // Permission is still not granted, handle this case accordingly
                finish() // Close the app when the user presses cancel in the Settings screen
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        // Stops the background service when the app is manually killed
//
//    }



}

