package com.example.smstoemail

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.security.crypto.MasterKeys
import com.example.smstoemail.Interfaces.smsFilterRecyclerMessageDao
import com.example.smstoemail.Permissions.CheckPermissions
import com.example.smstoemail.Repository.AppDatabase
import com.example.smstoemail.SmsFilters.SmsFilter
import com.example.smstoemail.SmsFilters.SmsFiltersAdapter
import kotlinx.coroutines.GlobalScope
import okhttp3.Dispatcher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class MyApplication : Application() {

    private var appOpenAd: AppOpenAd? = null // Initialize with null
    private lateinit var checkPermissions: CheckPermissions


    override fun onCreate() {
        super.onCreate()



        sharedPrefs = getSharedPreferences("preferences", MODE_PRIVATE)
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        encryptedSharedPrefs = EncryptedSharedPreferences.create(
            "encrypted_preferences",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val encryptionKey = generateEncryptionKey()
        val encryptionKeyString = Base64.encodeToString(encryptionKey.encoded, Base64.DEFAULT)
        if(!encryptedSharedPrefs.contains("encryptionKey")) {
            encryptedSharedPrefs.edit().putString("encryptionKey", encryptionKeyString).apply()
        }

        // initialize filter Adapter


        smsFilterAdapter = SmsFiltersAdapter()
        initializeSmsFilterRecyclerMessagesDao(this)

        sharedPrefs.edit().putBoolean("advertisementOff", false).apply()
        MainActivityUtils.handleSharedPreferencesOnInitialization(this)


        MainActivityUtils.startBackgroundService(this)




    }

    private fun loadAppOpenAd() {
        // Create an instance of AppOpenAd.
        AppOpenAd.load(
            this,
            getString(R.string.admob_app_open_test_id),
            AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle ad load error.
                }
            }
        )
    }
    fun showAppOpenAd() {
        appOpenAd?.show(this as Activity)
    }

    private fun generateEncryptionKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(algorithm)
        keyGenerator.init(256) // Key size in bits
        return keyGenerator.generateKey()
    }

    private fun initializeSmsFilterRecyclerMessagesDao(context: Context){
        GlobalScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getInstance(context)
            smsFilterRecyclerMessageDao = database.smsFilterRecyclerMessageDao()
            val smsFilterRecyclerMessages = smsFilterRecyclerMessageDao.getAllItems()
            smsFilterAdapter.updateSmsFilterList(smsFilterRecyclerMessages)
            // Use the 'items' in the UI if needed (e.g., update the UI with the data)
        }
    }


}