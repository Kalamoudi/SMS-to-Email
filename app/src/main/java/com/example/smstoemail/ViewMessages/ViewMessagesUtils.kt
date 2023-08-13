package com.example.smstoemail.ViewMessages

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smstoemail.R
import com.example.smstoemail.sharedPrefs

object ViewMessagesUtils {

    fun addAdvertisement(context: Context){
        if(sharedPrefs.getBoolean("advertisementOff", true)){
            return
        }

        val bigAdvertisement: View = (context as AppCompatActivity).findViewById(R.id.adViewInViewMessages)

        bigAdvertisement.visibility = View.VISIBLE


    }
}