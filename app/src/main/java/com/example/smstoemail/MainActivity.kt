package com.example.smstoemail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.smstoemail.Entity.RecyclerMessages
import com.example.smstoemail.Interfaces.ItemDao
import com.example.smstoemail.Pages.HandleMainPageViews
import com.example.smstoemail.Permissions.CheckPermissions
import com.example.smstoemail.Repository.AppDatabase
import com.example.smstoemail.Services.BackgroundService
import com.example.smstoemail.Sms.HandleSMS
import com.example.smstoemail.Utils
import com.google.android.material.navigation.NavigationView


// Kotlin imports

open class MainActivity : AppCompatActivity(){


    private lateinit var checkPermissions: CheckPermissions
    private lateinit var handleSMS: HandleSMS
    private lateinit var handleMainPageView: HandleMainPageViews
    private lateinit var drawerLayout : DrawerLayout
    lateinit var serviceIntent: Intent
    private lateinit var menuButton: Button
    private lateinit var toggleThemeButton: Button
    private lateinit var itemDao: ItemDao




    override fun onCreate(savedInstanceState: Bundle?) {

        // Set the theme of the app based on isNightMode trigger
        if (Utils.isNightMode()) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        // Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getInstance(this)
        itemDao = db.itemDao()
        val items = itemDao.getAllItems()


        // Applied logic to isNightTime and recreates the app for the visual appearance
        toggleThemeButton = findViewById(R.id.toggleThemeButton)
        toggleThemeButton.setOnClickListener {
            // Toggle the theme when the button is clicked
            Utils.setAppTheme(!Utils.isNightMode())

            recreate()
        }






        serviceIntent = Intent(this, BackgroundService::class.java)
        startService(serviceIntent)


        // Handles drawer functionality
        drawerLayout = findViewById(R.id.drawerLayout)
        val openSlidingButton = findViewById<Button>(R.id.openSlidingButton)
        val navigationDrawerLayout = findViewById<View>(R.id.navigationDrawer)
        menuButton = findViewById(R.id.menuButton)

        openSlidingButton.setOnClickListener {
            // Open the sliding window if the view is not null
            drawerLayout.openDrawer(navigationDrawerLayout)
        }

        menuButton.setOnClickListener {
            // Open the sliding window if the view is not null
            Log.d("MenuButton", "Menu button clicked!")
            drawerLayout.openDrawer(navigationDrawerLayout)
        }




        // HandlesSMS Receive and sending of Email
        handleSMS = HandleSMS()
        handleSMS.handleSMS(this)

        // Handle main page views
        handleMainPageView = HandleMainPageViews()
        Utils.checkSharedPreference(this, "app_force_stopped")
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
        if (!checkPermissions.allPermissionsGranted(this)) {
            checkPermissions.handlePermissionsResult(
                this,
                requestCode,
                permissionsList,
                grantResults
            )
        }
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

    override fun onDestroy() {
        super.onDestroy()

        // Stops the background service when the app is manually killed
        Log.d("RecyclerInformationSaved", "CheckingIfItWillSaveTheInformation")

        // Save a flag to SharedPreferences indicating the app was force-stopped
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("save", true)
        editor.apply()

    }


    private fun saveNewItem(recyclerMessage: RecyclerMessages) {
        itemDao.insert(recyclerMessage)
    }

}

