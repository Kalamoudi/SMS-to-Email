package com.example.smstoemail.Settings



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.preference.PreferenceFragmentCompat // Correct import statement
import androidx.preference.SwitchPreference
import com.example.smstoemail.R
import com.example.smstoemail.sharedPrefs

class SettingsFragment() : PreferenceFragmentCompat(){



    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)


        val switchPreference = findPreference<SwitchPreference>("settingsThemeSwitch")!!

      //  switchPreference.setDefaultValue(sharedPrefs.getBoolean("isNightMode", true))


        switchPreference.setOnPreferenceChangeListener { _, newValue ->

            val isNightMode = sharedPrefs.getBoolean("isNightMode", true)
            sharedPrefs.edit().putBoolean("isNightMode", !isNightMode).apply()
            //switchPreference.setDefaultValue(!isNightMode)



            //recreate(utilsContext as Activity)
           // recreate(context as Activity)
            updateTheme()
            recreate(context as AppCompatActivity)

            true

        }


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