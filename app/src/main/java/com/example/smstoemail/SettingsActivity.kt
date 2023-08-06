package com.example.smstoemail


import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.view.marginTop
import androidx.core.view.size
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
import com.example.smstoemail.Services.BackgroundService

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        MainActivityUtils.processAppTheme(this)

        setContentView(R.layout.activity_settings)




//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.settings_container, SettingsFragment())
//                .commit()
//        }



        var frameLayout = findViewById<FrameLayout>(R.id.settings_container)
        setsTopMarginForAllFramelayoutElements(frameLayout)

        processBackgroundCheckbox()


        processSettingsThemeSwitch()


    }

    fun setsTopMarginForAllFramelayoutElements(frameLayout: FrameLayout){

        // insitialize values in xml dp
        var initialMargin = (10 * resources.displayMetrics.density).toInt()
        val frameHeight = (75 * resources.displayMetrics.density).toInt()

        var index = 0
        for(frame in frameLayout){
            val layoutParams = frame.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = initialMargin + index*frameHeight
            frame.layoutParams = layoutParams
            index += 1
        }

    }

    private fun processBackgroundCheckbox(){

        val checkBoxLayout = findViewById<RelativeLayout>(R.id.checkBox1Layout)
        val checkBox1 = findViewById<CheckBox>(R.id.checkBox1)

        // Not redundant, kotlin trolling
        // Sets the checkbox on or off depending on whether the background service is on or off
        if(sharedPrefs.getBoolean("backgroundService", true)){
            checkBox1.isChecked = true
        }
        else{
            checkBox1.isChecked = false
        }

        val serviceIntent = Intent(this, BackgroundService::class.java)

        checkBoxLayout.setOnClickListener{
            if(sharedPrefs.getBoolean("backgroundService", true)){
                stopService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundServiceOn", false).apply()

            }
            else{
                startService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundServiceOn", true).apply()
            }
            sharedPrefs.edit().putBoolean("backgroundService", !sharedPrefs.getBoolean("backgroundService", true)).apply()
            checkBox1.isChecked = !checkBox1.isChecked
        }

        checkBox1.setOnClickListener{

            if(sharedPrefs.getBoolean("backgroundService", true)){
                stopService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundServiceOn", false).apply()
            }
            else{
                startService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundServiceOn", true).apply()
            }
            sharedPrefs.edit().putBoolean("backgroundService", !sharedPrefs.getBoolean("backgroundService", true)).apply()
        }

    }

    private fun processSettingsThemeSwitch(){

        val switchLayout = findViewById<RelativeLayout>(R.id.themSwitchLayout)
        val switchCompat = findViewById<SwitchCompat>(R.id.switchWidgetTest)
        switchLayout.setOnClickListener{
            val currentTheme = sharedPrefs.getBoolean("isNightMode", true)
            sharedPrefs.edit().putBoolean("isNightMode", !currentTheme).apply()
            switchCompat.isChecked = !switchCompat.isChecked



            updateTheme()
          //  recreate()

        }
        switchCompat.setOnClickListener{
            val currentTheme = sharedPrefs.getBoolean("isNightMode", true)
            sharedPrefs.edit().putBoolean("isNightMode", !currentTheme).apply()
            updateTheme()
            // recreate()

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateTheme(){
        val nightMode = sharedPrefs.getBoolean("isNightMode", true)

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}

