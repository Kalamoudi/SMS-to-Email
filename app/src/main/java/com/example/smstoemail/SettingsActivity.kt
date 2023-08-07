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
import androidx.activity.OnBackPressedCallback
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
import com.google.android.material.internal.ViewUtils.dpToPx

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        MainActivityUtils.processAppTheme(this)


        setContentView(R.layout.activity_settings)


        val backButton = findViewById<Button>(R.id.settingsBackButton)

        backButton.setOnClickListener{

            onBackPressed();

        }


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
        pressCheckbox3()


    }

    fun setsTopMarginForAllFramelayoutElements(frameLayout: FrameLayout){

        // insitialize values in xml dp
        var initialMargin = (90 * resources.displayMetrics.density).toInt()
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

        val checkbox1Layout = findViewById<RelativeLayout>(R.id.checkbox1Layout)
        val checkbox1 = findViewById<CheckBox>(R.id.checkbox1)

        checkbox1.isChecked = sharedPrefs.getBoolean("backgroundService", true)

        // Not redundant, kotlin trolling
        // Sets the checkbox on or off depending on whether the background service is on or off


     //   checkbox1.isChecked = sharedPrefs.getBoolean("backgroundService", true)

        val serviceIntent = Intent(this, BackgroundService::class.java)
        addHighlight(checkbox1Layout)

        checkbox1Layout.setOnClickListener{
            if(sharedPrefs.getBoolean("backgroundService", true)){
                stopService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundServiceOn", false).apply()

            }
            else{
                startService(serviceIntent)
                sharedPrefs.edit().putBoolean("backgroundServiceOn", true).apply()
            }
            sharedPrefs.edit().putBoolean("backgroundService", !sharedPrefs.getBoolean("backgroundService", true)).apply()
            checkbox1.isChecked = !checkbox1.isChecked

        }

//        checkbox1.setOnClickListener{
//
//            if(sharedPrefs.getBoolean("backgroundService", true)){
//                stopService(serviceIntent)
//                sharedPrefs.edit().putBoolean("backgroundServiceOn", false).apply()
//            }
//            else{
//                startService(serviceIntent)
//                sharedPrefs.edit().putBoolean("backgroundServiceOn", true).apply()
//            }
//            sharedPrefs.edit().putBoolean("backgroundService", !sharedPrefs.getBoolean("backgroundService", true)).apply()
//        }

    }

    private fun processSettingsThemeSwitch(){

        val switchLayout = findViewById<RelativeLayout>(R.id.themeSwitchLayout)
        val switchCompat = findViewById<SwitchCompat>(R.id.switchWidgetTest)

        addHighlight(switchLayout)
        switchLayout.setOnClickListener{
            val currentTheme = sharedPrefs.getBoolean("isNightMode", true)
            sharedPrefs.edit().putBoolean("isNightMode", !currentTheme).apply()
            switchCompat.isChecked = !switchCompat.isChecked


            updateTheme()
            recreate()


        }
//        switchCompat.setOnClickListener{
//            val currentTheme = sharedPrefs.getBoolean("isNightMode", true)
//            sharedPrefs.edit().putBoolean("isNightMode", !currentTheme).apply()
//            updateTheme()
//            recreate()
//
//        }
    }


    fun pressCheckbox3(){
        val checkbox2Layout = findViewById<RelativeLayout>(R.id.checkbox2Layout)
        val checkbox2 = findViewById<CheckBox>(R.id.checkbox2)

        addHighlight(checkbox2Layout)
        checkbox2Layout.setOnClickListener{

            checkbox2.isChecked = !checkbox2.isChecked


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

    private fun addHighlight(relativeLayout: RelativeLayout){

        relativeLayout.setBackgroundResource(R.drawable.background_clicked)
        relativeLayout.elevation = 20f.dpToPx(this)

        relativeLayout.postDelayed({
            relativeLayout.setBackgroundResource(R.drawable.background_normal)
            relativeLayout.elevation = 0f.dpToPx(this)
        }, 100000) // Delay in milliseconds
    }

    fun Float.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }
}

