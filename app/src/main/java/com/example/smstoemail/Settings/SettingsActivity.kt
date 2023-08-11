package com.example.smstoemail.Settings


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.iterator
import com.example.smstoemail.MainActivityUtils
import com.example.smstoemail.R
import com.example.smstoemail.Services.BackgroundService
import com.example.smstoemail.sharedPrefs
import com.example.smstoemail.utilsContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.app.ActivityCompat.recreate


class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsFragment: SettingsFragment
    private var isAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        MainActivityUtils.processAppTheme(this)


        setContentView(R.layout.activity_settings)


        val backButton = findViewById<Button>(R.id.settingsBackButton)
        val backButtonLayout = findViewById<RelativeLayout>(R.id.settingsBackButtonView)

        backButtonLayout.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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


        // Sets the checkbox on or off depending on whether the background service is on or off
        checkbox1.isChecked = sharedPrefs.getBoolean("backgroundService", true)


       // addHighlight(checkbox1Layout)

        SettingsUtils.processClickWithHighlight(this, checkbox1Layout){


            val serviceIntent = Intent(this, BackgroundService::class.java)
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


    }

    private fun processSettingsThemeSwitch(){

        val switchLayout = findViewById<RelativeLayout>(R.id.themeSwitchLayout)
        val switchCompat = findViewById<SwitchCompat>(R.id.switchWidgetTest)


        sharedPrefs.edit().putBoolean("changingTheme", true).apply()
        SettingsUtils.processClickWithHighlight(this, switchLayout) {

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



    private fun pressCheckbox3(){
        val checkbox2Layout = findViewById<RelativeLayout>(R.id.checkbox2Layout)
        val checkbox2 = findViewById<CheckBox>(R.id.checkbox2)

        if(!sharedPrefs.contains("useSmtp")){
            sharedPrefs.edit().putBoolean("useSmtp", false).apply()
        }

        checkbox2.isChecked = sharedPrefs.getBoolean("useSmtp", true)

        SettingsUtils.processClickWithHighlight(this, checkbox2Layout) {
            checkbox2.isChecked = !checkbox2.isChecked
            sharedPrefs.edit().putBoolean("useSmtp", !sharedPrefs.getBoolean("useSmtp", true)).apply()
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }


}

