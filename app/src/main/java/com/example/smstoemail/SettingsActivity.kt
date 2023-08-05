package com.example.smstoemail


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        MainActivityUtils.processAppTheme(this)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }

//        if (savedInstanceState == null) {
//            // Show the SettingsFragment in the Fragment container
//            supportFragmentManager.commit {
//                replace(R.id.settings_container, SettingsFragment())
//            }
//        }







    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

