package com.example.smstoemail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smstoemail.GoogleSignIn.SignInWithGmail
import com.example.smstoemail.NavigationDrawer.HandleNavDrawer
import com.example.smstoemail.Pages.HandleMainPageViews
import com.example.smstoemail.Permissions.CheckPermissions
import com.example.smstoemail.Sms.HandleSMS
import com.example.smstoemail.Utils.RC_SIGN_IN
import com.example.smstoemail.Utils.REQUEST_AUTHORIZATION
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient


// Kotlin imports

open class MainActivity : AppCompatActivity() {



    private lateinit var checkPermissions: CheckPermissions
    private lateinit var handleSMS: HandleSMS
    private lateinit var handleMainPageView: HandleMainPageViews
    private lateinit var handleNavDrawer : HandleNavDrawer
   // private lateinit var signInWithGmail: SignInWithGmail

    lateinit var serviceIntent: Intent
    private lateinit var menuButton: Button
    lateinit var drawerLayout: DrawerLayout

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    // Google Sign In varaibles and constants
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val REQ_ONE_TAP = 9002
    private var showOneTapUI = true

    // Ads

    private var appOpenAd: AppOpenAd? = null
    private lateinit var loadCallback: AppOpenAd.AppOpenAdLoadCallback





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // loadAppOpenAd()

        utilsContext = this
        // Instantiate the getSharedPreferences with tableName = "preferences
        sharedPrefs = getSharedPreferences("preferences", MODE_PRIVATE)


        // Handles all the sharedPreference logic (always check/edit after changing something to preference)
        MainActivityUtils.handleSharedPreferencesOnInitialization()

        sharedPrefs.edit().putBoolean("advertisementOff", true).apply()

        // Set the theme of the app based on isNightMode trigger
        MainActivityUtils.processAppTheme(this)


        setContentView(R.layout.activity_main)

        // Show main page bottom ad
        Utils.showAd(this, findViewById(R.id.adViewMediumRectangle))


   //     MainActivityUtils.addAdvertisement(this)

        // Check permissions
        checkPermissions = CheckPermissions()
        checkPermissions.handlePermissions(this)

        // Starts the BackgroundService
        MainActivityUtils.startBackgroundService(this)

        // Activate SignIn with google service
        signInWithGmail = SignInWithGmail()
        signInWithGmail.handleSignIn(this)
        //====================================================

        // HandlesSMS Receive and sending of Email
        handleSMS = HandleSMS()
        handleSMS.handleSMS(this)


        //========= Tool bar functionalities ================
        // Process and handles navigation drawer logic
        MainActivityUtils.processNavigationDrawer(this)
        handleNavDrawer = HandleNavDrawer(this)
        handleNavDrawer.handleNavDrawer()



        // Handle main page views
        handleMainPageView = HandleMainPageViews()
        Utils.checkSharedPreference(this, "app_force_stopped")
        handleMainPageView.handleViews(this)


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
//        if (requestCode == RC_SIGN_IN) {
//            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
//            if (result != null) {
//                handleGoogleSignInResult(result)
//            }
//        }
        if (requestCode == RC_SIGN_IN) {
            signInWithGmail.handleGoogleSignInResult(this, GoogleSignIn.getSignedInAccountFromIntent(data))
            recreate()
        }

        if (requestCode == REQUEST_AUTHORIZATION) {
            // Handle the result of user consent activity
            if (resultCode == Activity.RESULT_OK) {
                // User granted consent, you can now proceed with the operation
            } else {
                // User did not grant consent, handle as needed
            }
        }
    }





    override fun onDestroy() {
        super.onDestroy()

        // Stops the background service when the app is manually killed
        Log.d("RecyclerInformationSaved", "CheckingIfItWillSaveTheInformation")

        // Save a flag to SharedPreferences indicating the app was force-stopped
        sharedPrefs.edit().putBoolean("firstVisit", true).apply()
        sharedPrefs.edit().putBoolean("save", true).apply()

//        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putBoolean("save", true)
//        editor.apply()

    }

    override fun onResume() {
        super.onResume()

       // showAppOpenAd()

        // Re-register onBackPressed callback to ensure it works after coming back to the app
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
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


    override fun onStart(){
        super.onStart()
        //showAppOpenAd()
    }


//    private fun loadAppOpenAd() {
//        // Create an instance of AppOpenAd.
//        AppOpenAd.load(
//            this,
//            getString(R.string.admob_app_open_test_id),
//            AdRequest.Builder().build(),
//            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
//            object : AppOpenAd.AppOpenAdLoadCallback() {
//                override fun onAdLoaded(ad: AppOpenAd) {
//                    appOpenAd = ad
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    // Handle ad load error.
//                }
//            }
//        )
//    }
//    fun showAppOpenAd() {
//        appOpenAd?.show(this)
//    }

}



