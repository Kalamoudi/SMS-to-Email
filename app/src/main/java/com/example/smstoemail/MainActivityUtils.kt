package com.example.smstoemail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smstoemail.Interfaces.smtpDao
import com.example.smstoemail.Pages.HandleMainPageViews
import com.example.smstoemail.Repository.AppDatabase
import com.example.smstoemail.Services.BackgroundService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object MainActivityUtils {




    fun processAppTheme(context: Context){

        if(!sharedPrefs.contains("isNightMode")) {
            val systemThemeSetting =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            sharedPrefs.edit().putBoolean("isNightMode", systemThemeSetting == Configuration.UI_MODE_NIGHT_YES).apply()
        }

        if(sharedPrefs.getBoolean("isNightMode", true)){
            context.setTheme(R.style.AppTheme_Dark)
          //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            context.setTheme(R.style.AppTheme)
          //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


    }

    fun startBackgroundService(context: Context){

        val serviceIntent = Intent(context, BackgroundService::class.java)
        if(!sharedPrefs.contains("backgroundService")){
            context.startService(serviceIntent)
            sharedPrefs.edit().putBoolean("backgroundService", true).apply()
        }
    }




//    fun processSettingTheme(context: Context) {
//
//        val toggleThemeButton: Button = (context as AppCompatActivity).findViewById(R.id.toggleThemeButton)
//        toggleThemeButton.setOnClickListener {
//            // Toggle the theme when the button is clicked
//            Utils.setAppTheme(!Utils.isNightMode())
//            context.recreate()
//        }
//    }

    fun processNavigationDrawer(context: Context){
        val drawerLayout: DrawerLayout = (context as AppCompatActivity).findViewById(R.id.drawerLayout)
        val navigationDrawerLayout = context.findViewById<View>(R.id.navDrawer)
        val menuButton: Button = context.findViewById(R.id.menuButton)
        val menuButtonLayout: RelativeLayout = context.findViewById(R.id.menuButtonLayout)



        menuButtonLayout.setOnClickListener {
            // Open the sliding window if the view is not null
            Log.d("MenuButton", "Menu button clicked!")
            drawerLayout.openDrawer(navigationDrawerLayout)
        }
    }

    fun closeNavigationDrawer(context: Context){
        val drawerLayout: DrawerLayout = (context as AppCompatActivity).findViewById(R.id.drawerLayout)
        val navigationDrawerLayout = context.findViewById<View>(R.id.navDrawer)

        drawerLayout.closeDrawer(navigationDrawerLayout)
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, handle the account object (you may want to save user info)
            // For example, get user's email and display it:
            val userEmail = account?.email
        } catch (e: ApiException) {
            // The ApiException status code indicates the error reason.
            Log.e("GoogleSignIn", "signInResult:failed code=${e.statusCode}")
        }
    }


    fun handleSharedPreferencesOnInitialization(){

        if(!sharedPrefs.contains("firstVisit")){
            sharedPrefs.edit().putBoolean("firstVisit", true).apply()
        }
        if(!sharedPrefs.contains("changingTheme")){
            sharedPrefs.edit().putBoolean("changingTheme", false).apply()
        }

        if(!sharedPrefs.contains("useSmtp")){
            sharedPrefs.edit().putBoolean("useSmtp", false).apply()
        }

        if(!sharedPrefs.contains("passwordVisibility")){
            sharedPrefs.edit().putBoolean("passwordVisibility", false).apply()
        }

        if(!sharedPrefs.contains("accountIconColor")){
            sharedPrefs.edit().putInt("accountIconColor", 0).apply()
        }
        if(!sharedPrefs.contains("advertisementOff")){
            sharedPrefs.edit().putBoolean("advertisementOff", false).apply()
        }

        if(sharedPrefs.getBoolean("firstVisit", true)) {
            GlobalScope.launch(Dispatchers.IO) {
                val database = AppDatabase.getInstance(utilsContext)
                smtpDao = database.smtpDao()
                smtpDataList = smtpDao.getAllItems()
                // Use the 'items' in the UI if needed (e.g., update the UI with the data)
            }
            sharedPrefs.getBoolean("firstVisit", false)
        }

    }

    fun addAdvertisement(context: Context){
        if(sharedPrefs.getBoolean("advertisementOff", true)){
            return
        }

        val bigAdvertisement: View = (context as AppCompatActivity).findViewById(R.id.adViewInMain)

        bigAdvertisement.visibility = View.VISIBLE


    }

}