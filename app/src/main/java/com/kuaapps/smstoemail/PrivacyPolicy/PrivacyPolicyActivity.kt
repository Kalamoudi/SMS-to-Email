package com.kuaapps.smstoemail.PrivacyPolicy

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.kuaapps.smstoemail.MainActivityUtils
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Utils
import com.kuaapps.smstoemail.sharedPrefs

class PrivacyPolicyActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainActivityUtils.initializeTheme(this)
        setContentView(R.layout.activity_privacy_policy)

        val webView: WebView = findViewById(R.id.privacyPolicyWebView)

//        if (sharedPrefs.getBoolean("isNightMode", true)) {
//            webView.setBackgroundColor(resources.getColor(android.R.color.background_dark))
//            webView.settings.forceDark = WebSettings.FORCE_DARK_ON
//            webView.settings.javaScriptEnabled = true
//
//        }-

        webView.loadUrl("file:///android_asset/privacy_policy.txt")
    }



}