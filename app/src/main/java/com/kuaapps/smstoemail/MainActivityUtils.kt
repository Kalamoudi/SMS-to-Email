package com.kuaapps.smstoemail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.kuaapps.smstoemail.Interfaces.smtpDao
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Repository.AppDatabase
import com.kuaapps.smstoemail.Services.BackgroundService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object MainActivityUtils {




    fun initializeTheme(context: Context){

        if(sharedPrefs.getBoolean("isNightMode", true)){
            context.setTheme(R.style.AppTheme_Dark)
        }
        else{
            context.setTheme(R.style.AppTheme)
        }

    }

    fun updateTheme(context: Context){
        if(sharedPrefs.getBoolean("isNightMode", true)){
            context.setTheme(R.style.AppTheme_Dark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            context.setTheme(R.style.AppTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun startBackgroundService(context: Context){

        val serviceIntent = Intent(context, BackgroundService::class.java)
        if(sharedPrefs.getBoolean("backgroundService", true)){
            context.startService(serviceIntent)
        }
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
           // Log.d("MenuButton", "Menu button clicked!")
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


    fun handleSharedPreferencesOnInitialization(context: Context){

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
            sharedPrefs.edit().putBoolean("advertisementOff", true).apply()
        }
        if(!sharedPrefs.contains("isNightMode")) {
//            val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//            val systemNightMode = uiModeManager.nightMode
//
//            sharedPrefs.edit().putBoolean("isNightMode", systemNightMode == UiModeManager.MODE_NIGHT_YES).apply()
            sharedPrefs.edit().putBoolean("isNightMode", true).apply()
        }

        if(!sharedPrefs.contains("wifiOnly")){
            sharedPrefs.edit().putBoolean("wifiOnly", false).apply()
        }
        if(!sharedPrefs.contains("foregroundService")){
            sharedPrefs.edit().putBoolean("foregroundService", true).apply()
        }
        if(!sharedPrefs.contains("termsAgreement")){
            sharedPrefs.edit().putBoolean("termsAgreement", false).apply()
        }

        if(!sharedPrefs.contains("openAppOpenAd")){
            sharedPrefs.edit().putBoolean("openAppOpenAd", true).apply()
        }

        if(!sharedPrefs.contains("notifyBatteryOptimization")) {
            sharedPrefs.edit().putBoolean("notifyBatteryOptimization", true).apply()
        }
        if(sharedPrefs.getBoolean("firstVisit", true)) {
            GlobalScope.launch(Dispatchers.IO) {
                val database = AppDatabase.getInstance(context)
                smtpDao = database.smtpDao()
                smtpDataList = smtpDao.getAllItems()
                // Use the 'items' in the UI if needed (e.g., update the UI with the data)
            }
            sharedPrefs.getBoolean("firstVisit", false)
        }

    }


    fun showUserAgreementDialog(context: Context, receivedCode: () -> Unit) {
        val alertDialog = AlertDialog.Builder(context, Utils.getCurrentThemeAsInt())
            .setTitle("User Agreement")
            .setMessage(loadTermsAndAgreementTextFromAssets(context))
            .setPositiveButton("Accept") { _, _ ->
                sharedPrefs.edit().putBoolean("termsAgreement", true).apply()
                receivedCode()
            }
            .setNegativeButton("Decline") { _, _ ->
                Utils.closeAppCompletely(context)
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    private fun loadTermsAndAgreementTextFromAssets(context: Context): String {
        return try {
            val inputStream = context.assets.open("terms_and_agreement.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            reader.close()
            stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            "Error loading terms and agreement"
        }
    }



}