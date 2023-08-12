package com.example.smstoemail.NavigationDrawer

import android.app.AlertDialog
import android.content.Context
import android.content.res.AssetManager
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import com.example.smstoemail.sharedPrefs
import java.io.IOException

object NavDrawerUtils {

     fun showLicenseDialog(context: Context) {
        try {
            val assetManager: AssetManager = context.assets // Use applicationContext.assets instead of assets
            val inputStream = assetManager.open("LICENSE.txt")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val licenseContent = String(buffer, Charsets.UTF_8)

            val dialogBuilder = AlertDialog.Builder(context,  Utils.getCurrentThemeAsInt())
                .setTitle("License Information")
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
                R.drawable.ic_settings_filled
            )
        }
        return processedList
    }

//    fun readLicenseFile(context:Context): String {
//        return try {
//            val inputStream = context.assets.open("LICENSE.txt")
//            val size = inputStream.available()
//            val buffer = ByteArray(size)
//            inputStream.read(buffer)
//            inputStream.close()
//
//            String(buffer, Charsets.UTF_8)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            "License information not available."
//        }
//    }

}