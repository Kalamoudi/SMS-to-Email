package com.example.smstoemail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.Pages.HandleMainPageViews
import com.example.smstoemail.Permissions.CheckPermissions
import com.example.smstoemail.Services.BackgroundService
import com.example.smstoemail.Sms.HandleSMS



// Kotlin imports

class MainActivity : AppCompatActivity(){


    private lateinit var checkPermissions: CheckPermissions
    private lateinit var handleSMS: HandleSMS
    private lateinit var handleMainPageView: HandleMainPageViews



    // Google Sign-in variables
    //private lateinit var googleSignInClient: GoogleSignInClient
    //private val RC_SIGN_IN = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)




        // Checks for permissions
        checkPermissions = CheckPermissions()
        checkPermissions.handlePermissions(this)



        var serviceIntent = Intent(this, BackgroundService::class.java)
        startService(serviceIntent)


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

    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        // Stops the background service when the app is manually killed
//
//    }


}

