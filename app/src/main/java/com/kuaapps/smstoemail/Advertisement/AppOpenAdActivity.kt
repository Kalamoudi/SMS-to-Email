package com.kuaapps.smstoemail.Advertisement

import android.app.Activity

import android.os.Bundle

import com.google.android.gms.ads.appopen.AppOpenAd


open class AppOpenAdActivity : Activity() {

    private var appOpenAd: AppOpenAd? = null // Initialize with null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        finish()


    }



    override fun onStart(){
        super.onStart()

    }




}



