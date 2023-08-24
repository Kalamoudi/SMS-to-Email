package com.kuaapps.smstoemail

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.security.crypto.MasterKeys
import com.kuaapps.smstoemail.Interfaces.smsFilterRecyclerMessageDao
import com.kuaapps.smstoemail.Permissions.CheckPermissions
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Repository.AppDatabase
import com.kuaapps.smstoemail.SmsFilters.SmsFiltersAdapter
import kotlinx.coroutines.GlobalScope
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class MyApplication : Application() {

    private var appOpenAd: AppOpenAd? = null // Initialize with null
    private lateinit var checkPermissions: CheckPermissions


    override fun onCreate() {
        super.onCreate()

//        val intent = Intent(this, AppOpenAdActivity::class.java)
//        startActivity(intent)

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

        sharedPrefs.edit().putBoolean("advertisementOff", false).apply()
        //Utils.premium()

        smsFilterAdapter = SmsFiltersAdapter(smsFilterCapacity)
        initializeSmsFilterRecyclerMessagesDao(this)

        MainActivityUtils.handleSharedPreferencesOnInitialization(this)






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