package com.kuaapps.smstoemail.NavigationDrawer


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kuaapps.smstoemail.MainActivity
import com.kuaapps.smstoemail.MainActivityUtils
import com.kuaapps.smstoemail.PrivacyPolicy.PrivacyPolicyActivity
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Smtp.SmtpActivity
import com.kuaapps.smstoemail.Settings.SettingsActivity
import com.kuaapps.smstoemail.SmsFilters.SmsFiltersActivity
import com.kuaapps.smstoemail.Utils
import com.kuaapps.smstoemail.ViewMessages.ViewMessagesActivity
import org.w3c.dom.Text


class HandleNavDrawer (private val context: Context) {


    private val appCompatActivity: AppCompatActivity = context as AppCompatActivity

    private val mainListData = listOf("Home", "Received SMS", "Configure SMTP", "SMS Filters", "Settings")
    private val secondaryListData = listOf("Contact Us", "Privacy Policy", "License")
    private lateinit var drawerLayout: DrawerLayout

    private var mainListImages = listOf(
        R.drawable.ic_home,
        R.drawable.sms_40px,
        R.drawable.forward_to_inbox_40px,
        R.drawable.library_add_40px,
        R.drawable.ic_settings
    )



    private val mainListView: ListView =
        appCompatActivity.findViewById<ListView>(R.id.navDrawerMainList)

    private val secondaryListView: ListView =
        appCompatActivity.findViewById<ListView>(R.id.navDrawerSecondaryList)

    private val navHeader: RelativeLayout = (context as AppCompatActivity).findViewById(R.id.navigationDrawerTop)



    fun handleNavDrawer() {


        val account = GoogleSignIn.getLastSignedInAccount(context)

        if(account != null){
            val navHeaderAccountInfo: RelativeLayout = (context as AppCompatActivity).findViewById(R.id.navDrawerHeaderAccountInfo)
            val navHeaderFullName: TextView = (context as AppCompatActivity).findViewById(R.id.navDrawerHeaderFullName)
            val navHeaderEmail: TextView = (context as AppCompatActivity).findViewById(R.id.navDrawerHeaderEmail)

            navHeaderFullName.text = account.displayName.toString()
            navHeaderEmail.text = account.email.toString()
            navHeaderAccountInfo.visibility = View.VISIBLE

        }



        drawerLayout = (context as AppCompatActivity).findViewById(R.id.drawerLayout)
      //  NavDrawerUtils.addAdvertisement(context)
        Utils.showAd(context, context.findViewById(R.id.adViewNavBanner))

        Utils.addBottomMarginForAd(
            context,
            (context as AppCompatActivity).findViewById<NestedScrollView>(R.id.navDrawerListsScrollView),
            R.dimen.viewMessagesPageRelativeLayoutMarginBottom
        )


        mainListImages = NavDrawerUtils.changeListToNight(mainListImages)
        val mainListAdapter = NavDrawerMainAdapter(context, mainListData, mainListImages)
        mainListView.adapter = mainListAdapter

        mainListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

            when(selectedItem){
                "Home" -> goBackToMainPage(context)
                "Received SMS" -> openViewMessagesPage(context)
                "Configure SMTP" -> openSmtpPage(context)
                "SMS Filters" -> openSmsFiltersPage(context)
                "Settings" -> openSettingsPage(context)
            }
        }

        val secondaryListAdapter = NavDrawerSecondAdapter(context, secondaryListData)
        secondaryListView.adapter = secondaryListAdapter

        secondaryListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

            when(selectedItem){
               // "Contact Us" -> Utils.showToast(context, "email: smstoemail.smssender@gmail.com")
                "Contact Us" -> NavDrawerUtils.showDialog(context, "contact_us.txt", "Contact Us")
             //   "Privacy Policy" -> NavDrawerUtils.showDialog(context, "privacy_policy.txt", "Privacy Policy")
                "Privacy Policy" -> openPrivacyPolicyPage(context)
               // "Privacy Policy" -> openPrivacyPolicyPage(context)
                "License" -> NavDrawerUtils.showDialog(
                    context,
                    "LICENSE.txt",
                    "License Information"
                )
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
      //  drawerLayout.closeDrawers()
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
      //  drawerLayout.closeDrawers()
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
     //   drawerLayout.closeDrawers()

    }

    private fun openSmsFiltersPage(context: Context){
        val intent = Intent(context, SmsFiltersActivity::class.java)
        if(context::class.java == SmsFiltersActivity::class.java){
            return
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        MainActivityUtils.closeNavigationDrawer(context)
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none)
      //  drawerLayout.closeDrawers()

    }

    private fun openPrivacyPolicyPage(context: Context){
        if(context::class.java == PrivacyPolicyActivity::class.java){
            return
        }
        val url = "http://sms2email.kuaapps.com/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)


    }

}