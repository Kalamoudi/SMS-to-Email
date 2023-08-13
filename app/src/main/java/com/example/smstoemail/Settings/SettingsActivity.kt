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
import com.example.smstoemail.Utils


class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsFragment: SettingsFragment
    private var isAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        MainActivityUtils.processAppTheme(this)


        setContentView(R.layout.activity_settings)

        SettingsUtils.addAdvertisement(this)


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

        SettingsOptions.processBackgroundCheckbox(this)
        SettingsOptions.processSettingsThemeSwitch(this)
        SettingsOptions.processSmtpCheckbox(this)


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

