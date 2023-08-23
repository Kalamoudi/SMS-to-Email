package com.example.smstoemail.Advertisement

import com.example.smstoemail.MainActivityUtils
import com.example.smstoemail.R

import android.app.Activity

import android.os.Bundle

import com.google.android.gms.ads.AdRequest

import com.google.android.gms.ads.LoadAdError

import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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



