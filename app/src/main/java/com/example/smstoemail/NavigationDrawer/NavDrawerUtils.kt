package com.example.smstoemail.NavigationDrawer

import android.app.AlertDialog
import android.content.Context
import android.content.res.AssetManager
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import com.example.smstoemail.sharedPrefs
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object NavDrawerUtils {

     fun showDialog(context: Context, fileName: String, Title: String) {
        try {
            val assetManager: AssetManager = context.assets // Use applicationContext.assets instead of assets
            val inputStream = assetManager.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val licenseContent = String(buffer, Charsets.UTF_8)

            val dialogBuilder = AlertDialog.Builder(context,  Utils.getCurrentThemeAsInt())
                .setTitle(Title)
                .setMessage(licenseContent)
                .setPositiveButton("OK", null)

            val dialog = dialogBuilder.create()

            // Set the window background color to fully opaque black
            if(!sharedPrefs.getBoolean("isNightMode", true)) {
                dialog.window?.setBackgroundDrawableResource(android.R.color.white)
            }
            dialog.window?.setDimAmount(0f)


            dialog.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun changeListToNight(mainListImages: List<Int>): List<Int>{

        var processedList = mainListImages
        if(sharedPrefs.getBoolean("isNightMode", true)){
            processedList = listOf(
                R.drawable.ic_home_filled,
                R.drawable.sms_40px_filled,
                R.drawable.forward_to_inbox_40px_filled,
                R.drawable.library_add_40px_filled,
                R.drawable.ic_settings_filled,
            )
        }
        return processedList
    }


}