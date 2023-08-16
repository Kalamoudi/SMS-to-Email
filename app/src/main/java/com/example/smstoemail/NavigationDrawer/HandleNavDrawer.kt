package com.example.smstoemail.NavigationDrawer


import android.content.Context
import android.content.Intent
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smstoemail.MainActivity
import com.example.smstoemail.MainActivityUtils
import com.example.smstoemail.R
import com.example.smstoemail.Smtp.SmtpActivity
import com.example.smstoemail.Settings.SettingsActivity
import com.example.smstoemail.Utils
import com.example.smstoemail.ViewMessages.ViewMessagesActivity
import com.example.smstoemail.sharedPrefs


class HandleNavDrawer (private val context: Context) {


    private val appCompatActivity: AppCompatActivity = context as AppCompatActivity

    private val mainListData = listOf("Home", "Received SMS", "Configure SMTP", "Settings")
    private val secondaryListData = listOf("Contact us", "Privacy policy")

    private var mainListImages = listOf(
        R.drawable.ic_home,
        R.drawable.sms_40px,
        R.drawable.forward_to_inbox_40px,
        R.drawable.ic_settings
    )



    private val mainListView: ListView =
        appCompatActivity.findViewById<ListView>(R.id.navDrawerMainList)

    private val secondaryListView: ListView =
        appCompatActivity.findViewById<ListView>(R.id.navDrawerSecondaryList)



    fun handleNavDrawer() {

      //  NavDrawerUtils.addAdvertisement(context)
        Utils.showAd(context, (context as AppCompatActivity).findViewById(R.id.adViewNavBanner))


        mainListImages = NavDrawerUtils.changeListToNight(mainListImages)
        val mainListAdapter = NavDrawerMainAdapter(context, mainListData, mainListImages)
        mainListView.adapter = mainListAdapter

        mainListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

            when(selectedItem){
                "Home" -> goBackToMainPage(context)
                "Received SMS" -> openViewMessagesPage(context)
                "Configure SMTP" -> openSmtpPage(context)
                "Settings" -> openSettingsPage(context)
            }
        }

        val secondaryListAdapter = NavDrawerSecondAdapter(context, secondaryListData)
        secondaryListView.adapter = secondaryListAdapter

        secondaryListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

            when(selectedItem){
                "Contact us" -> Utils.showToast(context, "email: smstoemail.smssender@gmail.com")
                "Privacy policy" -> NavDrawerUtils.showLicenseDialog(context)
            }
        }


    }

    private fun goBackToMainPage(context: Context){
        if (context::class.java == MainActivity::class.java) {
            return
        }
       // (context as AppCompatActivity).finish()
        val intent = Intent(context, MainActivity::class.java)
      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_none, R.anim.slide_none)
    }

    private fun openSettingsPage(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        context.startActivity(intent)
       // (context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none)
    }

    private fun openSmtpPage(context: Context){
        val intent = Intent(context, SmtpActivity::class.java)
        if (context::class.java == SmtpActivity::class.java) {
            return
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        MainActivityUtils.closeNavigationDrawer(context)
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none)
    }

    private fun openViewMessagesPage(context: Context){
        val intent = Intent(context, ViewMessagesActivity::class.java)
        if(context::class.java == ViewMessagesActivity::class.java){
            return
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        MainActivityUtils.closeNavigationDrawer(context)
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none)

    }

}