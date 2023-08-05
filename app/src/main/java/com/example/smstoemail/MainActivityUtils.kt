package com.example.smstoemail

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView


object MainActivityUtils {

    fun processAppTheme(context: Context){
        if (Utils.isNightMode()) {
            context.setTheme(R.style.AppTheme_Dark)
        } else {
            context.setTheme(R.style.AppTheme)
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
        val openSlidingButton = context.findViewById<Button>(R.id.openSlidingButton)
        val navigationDrawerLayout = context.findViewById<View>(R.id.navDrawer)
        val menuButton: Button = context.findViewById(R.id.menuButton)

        openSlidingButton.setOnClickListener {
            // Open the sliding window if the view is not null
            drawerLayout.openDrawer(navigationDrawerLayout)
        }

        menuButton.setOnClickListener {
            // Open the sliding window if the view is not null
            Log.d("MenuButton", "Menu button clicked!")
            drawerLayout.openDrawer(navigationDrawerLayout)
        }

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

}