package com.example.smstoemail.Smtp

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smstoemail.R
import com.example.smstoemail.sharedPrefs

object SmtpUtils {

    fun addAdvertisement(context: Context){
        if(sharedPrefs.getBoolean("advertisementOff", true)){
            return
        }

        val smallAdvertisement: View = (context as AppCompatActivity).findViewById(R.id.adViewInSmtp)

        smallAdvertisement.visibility = View.VISIBLE


    }

}