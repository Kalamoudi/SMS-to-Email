package com.example.smstoemail.Settings

import android.content.Context
import android.content.Intent
import android.widget.CheckBox
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.smstoemail.R
import com.example.smstoemail.Services.BackgroundService
import com.example.smstoemail.sharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object SettingsOptions {

    private var isAnimating = false

    fun processBackgroundCheckbox(context: Context){

        val contextAsAppCompatActivity = context as AppCompatActivity
        val checkbox1Layout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.checkbox1Layout)
        val checkbox1 = contextAsAppCompatActivity.findViewById<CheckBox>(R.id.checkbox1)


        // Sets the checkbox on or off depending on whether the background service is on or off
        checkbox1.isChecked = sharedPrefs.getBoolean("backgroundService", true)


        // addHighlight(checkbox1Layout)

        SettingsUtils.processClickWithHighlight(context, checkbox1Layout){


            val serviceIntent = Intent(context, BackgroundService::class.java)
            if(sharedPrefs.getBoolean("backgroundService", true)){
                context.stopService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundServiceOn", false).apply()

            }
            else{
                context.startService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundServiceOn", true).apply()
            }
            sharedPrefs.edit().putBoolean("backgroundService", !sharedPrefs.getBoolean("backgroundService", true)).apply()
            checkbox1.isChecked = !checkbox1.isChecked

        }


    }


    fun processSettingsThemeSwitch(context: Context){

        val contextAsAppCompatActivity = context as AppCompatActivity
        val switchLayout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.themeSwitchLayout)
        val switchCompat = contextAsAppCompatActivity.findViewById<SwitchCompat>(R.id.switchWidgetTest)


        sharedPrefs.edit().putBoolean("changingTheme", true).apply()
        SettingsUtils.processClickWithHighlight(context, switchLayout) {

            if (isAnimating) {
                return@processClickWithHighlight // Do nothing if animation is ongoing
            }


            CoroutineScope(Dispatchers.Main).launch {
                if (isAnimating) {
                    return@launch // Do nothing if animation is ongoing
                }

                isAnimating = true
                val currentTheme = sharedPrefs.getBoolean("isNightMode", true)
                // sharedPrefs.edit().putBoolean("isNightMode", !currentTheme).apply()
                switchCompat.isChecked = !switchCompat.isChecked
                //    isAnimating = true
                switchCompat.animate()
                    .alpha(1f)
                    .setDuration(200L)
                    .start()
                delay(300)
                SettingsUtils.updateSettingsTheme(switchCompat)
                //  recreate(utilsContext as Activity)
                //   recreate()

                isAnimating = false
            }

        }

    }


    fun processSmtpCheckbox(context: Context){

        val contextAsAppCompatActivity = context as AppCompatActivity
        val checkbox2Layout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.checkbox2Layout)
        val checkbox2 = contextAsAppCompatActivity.findViewById<CheckBox>(R.id.checkbox2)

        checkbox2.isChecked = sharedPrefs.getBoolean("useSmtp", true)

        SettingsUtils.processClickWithHighlight(context, checkbox2Layout) {
            checkbox2.isChecked = !checkbox2.isChecked
            sharedPrefs.edit().putBoolean("useSmtp", !sharedPrefs.getBoolean("useSmtp", true)).apply()
        }

    }


}