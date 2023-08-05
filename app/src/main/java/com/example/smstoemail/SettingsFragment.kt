package com.example.smstoemail



import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.core.app.ActivityCompat.recreate
import androidx.preference.PreferenceFragment
import androidx.preference.PreferenceFragmentCompat // Correct import statement
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment() : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Apply the custom style to the PreferenceScreen
//        preferenceScreen?.apply {
//            context?.theme?.applyStyle(R.style.PreferenceStyle, true)
//        }
        val switchPreference = findPreference<SwitchPreference>("settingsThemeSwitch")

        val sharedPrefs = utilsContext.getSharedPreferences("preferences", Context.MODE_PRIVATE)

        sharedPrefs.edit().putBoolean("settingsThemeSwitch", isNightMode)

        switchPreference?.setOnPreferenceChangeListener { _, newValue ->
            // Do something with the new value here, e.g., enable/disable dark theme

                val currentVal = sharedPrefs.getBoolean("settingsThemeSwitch", true)
                sharedPrefs.edit().putBoolean("settingsThemeSwitch", !currentVal).apply()
                isNightMode = sharedPrefs.getBoolean("settingsThemeSwitch", true)


                recreate(utilsContext as Activity)
                recreate(context as Activity)

            true // Return 'true' to allow the preference to change its value.
        }

    }
}