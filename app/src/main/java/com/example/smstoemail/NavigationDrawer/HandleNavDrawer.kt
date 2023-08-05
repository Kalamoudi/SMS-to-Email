package com.example.smstoemail.NavigationDrawer


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.smstoemail.MainActivity
import com.example.smstoemail.R
import com.example.smstoemail.SettingsActivity
import com.example.smstoemail.Utils


class HandleNavDrawer (private val context: Context) {

    private val appCompatActivity: AppCompatActivity = context as AppCompatActivity

    private val mainListData = listOf("Home", "Item 2", "Settings")
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
            if (selectedItem.equals("Settings")) {
                openSettingsPage(context)
            }
            else if(selectedItem.equals("Home")){
                goBackToMainPage(context)
            }
        }

        val secondaryListAdapter = NavDrawerSecondAdapter(context, secondaryListData)
        secondaryListView.adapter = secondaryListAdapter

        secondaryListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            if (selectedItem.equals("Contact us")) {
                Utils.showToast(context, "email: khalidsmssender@gmail.com")
            }
            else if(selectedItem.equals("Privacy policy")){
                //NavDrawerUtils.readLicenseFile(context)
                NavDrawerUtils.showLicenseDialog(context)
            }
        }


    }

    private fun goBackToMainPage(context: Context){
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

    private fun openSettingsPage(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }

}