package com.kuaapps.smstoemail.Settings

import android.content.Context
import android.content.Intent
import android.widget.CheckBox
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.kuaapps.smstoemail.MainActivityUtils
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Services.BackgroundService
import com.kuaapps.smstoemail.Utils
import com.kuaapps.smstoemail.sharedPrefs
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

        SettingsUtils.processClickWithHighlight(context, checkbox1Layout) {


            val serviceIntent = Intent(context, BackgroundService::class.java)
            if (sharedPrefs.getBoolean("backgroundService", true)) {
                context.stopService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundService", false).apply()

            } else {
                context.startService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundService", true).apply()
            }

            checkbox1.isChecked = !checkbox1.isChecked

        }


    }

    fun processForegroundService(context: Context){

        val contextAsAppCompatActivity = context as AppCompatActivity
        val foregroundServiceCheckboxLayout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.foregroundServiceCheckboxLayout)
        val foregroundServiceCheckbox = contextAsAppCompatActivity.findViewById<CheckBox>(R.id.foregroundServiceCheckbox)

        val serviceIntent = Intent(context, BackgroundService::class.java)


        foregroundServiceCheckbox.isChecked = sharedPrefs.getBoolean("foregroundService", true)

        SettingsUtils.processClickWithHighlight(context, foregroundServiceCheckboxLayout) {
            foregroundServiceCheckbox.isChecked = !foregroundServiceCheckbox.isChecked
            sharedPrefs.edit()
                .putBoolean("foregroundService", !sharedPrefs.getBoolean("foregroundService", true))
                .apply()
            context.stopService(serviceIntent)
            context.startService(serviceIntent)
        }

    }


    fun processSettingsThemeSwitch(context: Context){

        val contextAsAppCompatActivity = context as AppCompatActivity
        val switchLayout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.themeSwitchLayout)
        val switchCompat = contextAsAppCompatActivity.findViewById<SwitchCompat>(R.id.switchWidgetTest)


      //  sharedPrefs.edit().putBoolean("changingTheme", true).apply()
        switchCompat.isChecked = sharedPrefs.getBoolean("isNightMode", true)

       // Log.d("CURRENTLY DARK THEME", sharedPrefs.getBoolean("isNightMode", true).toString())

        SettingsUtils.processClickWithHighlight(context, switchLayout) {

            if (isAnimating) {
                return@processClickWithHighlight // Do nothing if animation is ongoing
            }

            CoroutineScope(Dispatchers.Main).launch {

                if (isAnimating) {
                    return@launch // Do nothing if animation is ongoing
                }

                isAnimating = true
                sharedPrefs.edit()
                    .putBoolean("isNightMode", !sharedPrefs.getBoolean("isNightMode", true)).apply()
                switchCompat.isChecked = sharedPrefs.getBoolean("isNightMode", true)
                //    isAnimating = true
                switchCompat.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
                // delay(150L)
                // SettingsUtils.updateSettingsTheme()
                //  MainActivityUtils.updateTheme(context)
                //  recreate(context as Activity)
                //    recreate()
                delay(300)
                MainActivityUtils.updateTheme(context)
                delay(50L)
                isAnimating = false

            }
            //   MainActivityUtils.updateTheme(context)


        }

    }


    fun processSmtpCheckbox(context: Context){

        val contextAsAppCompatActivity = context as AppCompatActivity
        val checkbox2Layout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.checkbox2Layout)
        val checkbox2 = contextAsAppCompatActivity.findViewById<CheckBox>(R.id.checkbox2)

        checkbox2.isChecked = sharedPrefs.getBoolean("useSmtp", true)

        SettingsUtils.processClickWithHighlight(context, checkbox2Layout) {
            checkbox2.isChecked = !checkbox2.isChecked
            sharedPrefs.edit().putBoolean("useSmtp", !sharedPrefs.getBoolean("useSmtp", true))
                .apply()
        }

    }

    fun processGmailCheckbox(context: Context){

        val contextAsAppCompatActivity = context as AppCompatActivity
        val gmailCheckboxLayout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.gmailCheckboxLayout)
        val gmailCheckbox = contextAsAppCompatActivity.findViewById<CheckBox>(R.id.gmailCheckbox)

        gmailCheckbox.isChecked = sharedPrefs.getBoolean("useGmail", true)

        SettingsUtils.processClickWithHighlight(context, gmailCheckboxLayout) {
            gmailCheckbox.isChecked = !gmailCheckbox.isChecked
            sharedPrefs.edit().putBoolean("useGmail", !sharedPrefs.getBoolean("useGmail", true))
                .apply()
        }

    }

    fun processWifiOnlyCheckbox(context: Context){

        val contextAsAppCompatActivity = context as AppCompatActivity
        val wifiOnlyCheckboxLayout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.onlyWifiCheckboxLayout)
        val wifiOnlyCheckbox = contextAsAppCompatActivity.findViewById<CheckBox>(R.id.onlyWifiCheckbox)

        wifiOnlyCheckbox.isChecked = sharedPrefs.getBoolean("wifiOnly", true)

        SettingsUtils.processClickWithHighlight(context, wifiOnlyCheckboxLayout) {
            wifiOnlyCheckbox.isChecked = !wifiOnlyCheckbox.isChecked
            sharedPrefs.edit().putBoolean("wifiOnly", !sharedPrefs.getBoolean("wifiOnly", true))
                .apply()
        }

    }

    fun processBatteryOptimizationCheckbox(context: Context){
        val contextAsAppCompatActivity = context as AppCompatActivity
        val batteryOptimizationCheckboxLayout = contextAsAppCompatActivity.findViewById<RelativeLayout>(R.id.settingsBatteryOptimizationCheckBoxLayout)
        val batteryOptimizationCheckbox = contextAsAppCompatActivity.findViewById<CheckBox>(R.id.settingsBatteryOptimizationCheckBox)

        batteryOptimizationCheckbox.isChecked = sharedPrefs.getBoolean("notifyBatteryOptimization", true)

        SettingsUtils.processClickWithHighlight(context, batteryOptimizationCheckboxLayout) {
            batteryOptimizationCheckbox.isChecked = !batteryOptimizationCheckbox.isChecked
            sharedPrefs.edit().putBoolean("notifyBatteryOptimization", !sharedPrefs.getBoolean("notifyBatteryOptimization", true))
                .apply()
        }

    }


}