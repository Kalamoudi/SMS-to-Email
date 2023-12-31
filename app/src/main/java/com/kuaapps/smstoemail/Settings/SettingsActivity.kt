package com.kuaapps.smstoemail.Settings


import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.kuaapps.smstoemail.MainActivityUtils
import com.kuaapps.smstoemail.R
import androidx.core.widget.NestedScrollView
import com.kuaapps.smstoemail.Utils


class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsFragment: SettingsFragment
    private var isAnimating = false
    private lateinit var backButtonLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        MainActivityUtils.initializeTheme(this)
        super.onCreate(savedInstanceState)





        setContentView(R.layout.activity_settings)

        Utils.showAd(this, findViewById(R.id.adViewInSettings))

        Utils.addBottomMarginForAd(this, findViewById<NestedScrollView>(R.id.settingsPage),
                R.dimen.viewMessagesPageRelativeLayoutMarginBottom)


        val backButton = findViewById<Button>(R.id.settingsBackButton)
        backButtonLayout = findViewById<RelativeLayout>(R.id.settingsBackButtonView)


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

        var frameLayout = findViewById<FrameLayout>(R.id.settingsFrameLayout)
        setsTopMarginForAllFramelayoutElements(frameLayout)

        SettingsOptions.processBackgroundCheckbox(this)
        SettingsOptions.processForegroundService(this)
        SettingsOptions.processSmtpCheckbox(this)
        SettingsOptions.processGmailCheckbox(this)
        SettingsOptions.processWifiOnlyCheckbox(this)
        SettingsOptions.processSettingsThemeSwitch(this)
        SettingsOptions.processBatteryOptimizationCheckbox(this)


    }

    fun setsTopMarginForAllFramelayoutElements(frameLayout: FrameLayout){

        // insitialize values in xml dp
        var initialMargin = 0
        val frameHeight = (75 * resources.displayMetrics.density).toInt()

        var index = 0
        for(frame in frameLayout){
            val layoutParams = frame.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = initialMargin + index*frameHeight
            frame.layoutParams = layoutParams
            index += 1
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //backButtonLayout.performClick()
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }


}

