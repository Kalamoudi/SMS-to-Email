package com.example.smstoemail.NavigationDrawer


import android.content.Context
import android.content.Intent
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smstoemail.MainActivity
import com.example.smstoemail.MainActivityUtils
import com.example.smstoemail.R
import com.example.smstoemail.SMTP.SMPTActivity
import com.example.smstoemail.Settings.SettingsActivity
import com.example.smstoemail.Utils


class HandleNavDrawer (private val context: Context) {

    private val appCompatActivity: AppCompatActivity = context as AppCompatActivity

    private val mainListData = listOf("Home", "Configure SMTP", "Settings")
    private val secondaryListData = listOf("Contact us", "Privacy policy")

    private val mainListImages = listOf(
        R.drawable.ic_home,
        R.drawable.ic_home,
        R.drawable.ic_settings
    )

    private val mainListView: ListView =
        appCompatActivity.findViewById<ListView>(R.id.navDrawerMainList)

    private val secondaryListView: ListView =
        appCompatActivity.findViewById<ListView>(R.id.navDrawerSecondaryList)

    fun handleNavDrawer() {
        val mainListAdapter = NavDrawerMainAdapter(context, mainListData, mainListImages)
        mainListView.adapter = mainListAdapter

        mainListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

            when(selectedItem){
                "Home" -> goBackToMainPage(context)
                "Configure SMTP" -> openSmtpPage(context)
                "Settings" -> openSettingsPage(context)
            }
        }

        val secondaryListAdapter = NavDrawerSecondAdapter(context, secondaryListData)
        secondaryListView.adapter = secondaryListAdapter

        secondaryListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

            when(selectedItem){
                "Contact us" -> Utils.showToast(context, "email: khalidsmssender@gmail.com")
                "Privacy policy" -> NavDrawerUtils.showLicenseDialog(context)
            }
        }


    }

    private fun goBackToMainPage(context: Context){
        if (context::class.java == MainActivity::class.java) {
            return
        }
        (context as AppCompatActivity).finish()
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun openSettingsPage(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun openSmtpPage(context: Context){
        val intent = Intent(context, SMPTActivity::class.java)
        if (context::class.java == SMPTActivity::class.java) {
            return
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        MainActivityUtils.closeNavigationDrawer(context)
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}