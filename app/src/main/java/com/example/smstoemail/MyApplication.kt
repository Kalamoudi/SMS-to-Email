package com.example.smstoemail

import android.app.Activity
import android.app.Application
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class MyApplication : Application() {

    private var appOpenAd: AppOpenAd? = null // Initialize with null

    override fun onCreate() {
        super.onCreate()

        val context = this
        MobileAds.initialize(context)

        CoroutineScope(Dispatchers.Main).launch {
            loadAppOpenAd()
            showAppOpenAd()

        }
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


}