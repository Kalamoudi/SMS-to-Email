package com.example.smstoemail

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smstoemail.NavigationDrawer.HandleNavDrawer
import com.example.smstoemail.Pages.HandleMainPageViews
import com.example.smstoemail.Permissions.CheckPermissions
import com.example.smstoemail.Services.BackgroundService
import com.example.smstoemail.Sms.HandleSMS
import com.example.smstoemail.databinding.ActivitySettingsBinding
import com.example.smstoemail.databinding.CheckboxPreferenceBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Tasks


// Kotlin imports

open class MainActivity : AppCompatActivity() {



    private lateinit var checkPermissions: CheckPermissions
    private lateinit var handleSMS: HandleSMS
    private lateinit var handleMainPageView: HandleMainPageViews
    private lateinit var handleNavDrawer : HandleNavDrawer
    lateinit var serviceIntent: Intent
    private lateinit var menuButton: Button

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    // Google Sign In varaibles and constants
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private val REQ_ONE_TAP = 9002
    private var showOneTapUI = true





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        utilsContext = this

        // Instantiate the getSharedPreferences with tableName = "preferences
        sharedPrefs = getSharedPreferences("preferences", MODE_PRIVATE)

        // Set the theme of the app based on isNightMode trigger
        MainActivityUtils.processAppTheme(this)

        // Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)


        // Handles logic to set theme
       // MainActivityUtils.processSettingTheme(this)

        MainActivityUtils.processNavigationDrawer(this)


        //========================================================================
        //===========================================================================
     //   val webClientId = "615818751861-81s1lke2k29qvqimtci9o23heqgfm23f.apps.googleusercontent.com"

//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.my_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build())
//            // Automatically sign in when exactly one credential is retrieved.
//            .setAutoSelectEnabled(true)
//            .build()
//
//        oneTapClient.beginSignIn(signInRequest)
//            .addOnSuccessListener(this) { result ->
//                try {
//                    startIntentSenderForResult(
//                        result.pendingIntent.intentSender, REQ_ONE_TAP,
//                        null, 0, 0, 0, null)
//                } catch (e: IntentSender.SendIntentException) {
//                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
//                }
//            }
//            .addOnFailureListener(this) { e ->
//                // No saved credentials found. Launch the One Tap sign-up flow, or
//                // do nothing and continue presenting the signed-out UI.
//                Log.d(TAG, e.localizedMessage)
//            }


        //========================================================================
        //===========================================================================

        handleNavDrawer = HandleNavDrawer(this)
        handleNavDrawer.handleNavDrawer()




        // Starts the BackgroundService


        MainActivityUtils.startBackgroundService(this)


        // Handles Navigation Drawer functionality
      //  MainActivityUtils.processNavigationDrawer(this)


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
        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                Tasks.await(task)
                MainActivityUtils.handleSignInResult(task)
                Utils.showToast(this, "Signed In Successfully")
            } catch (e: ApiException) {
                // Handle sign-in failure here
                Log.e("GoogleSignIn", "signInResult:failed code=${e.statusCode}")
            }
        }
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d(TAG, "Got ID token.")
                        }
                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(TAG, "Couldn't get credential from result." +
                                    " (${e.localizedMessage})")
                        }
                    }
                }
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

    override fun onResume() {
        super.onResume()

        // Re-register onBackPressed callback to ensure it works after coming back to the app
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
                val navigationDrawerLayout = findViewById<View>(R.id.navDrawer)

                if (drawerLayout.isDrawerOpen(navigationDrawerLayout)) {
                    // Close the navigation drawer if it's open
                    drawerLayout.closeDrawer(navigationDrawerLayout)
                } else {
                    // If the navigation drawer is not open, proceed with the default back button behavior
                    isEnabled = false
                    onBackPressed()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }


}



