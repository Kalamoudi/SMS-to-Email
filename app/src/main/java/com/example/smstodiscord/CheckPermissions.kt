package com.example.smstodiscord

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.telephony.SmsMessage

class CheckPermissions {

    private val SMS_PERMISSION_REQUEST_CODE = 101

    // Checks for SMS permissions
    fun checkSmsPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.RECEIVE_SMS),
                    SMS_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            showToast(context,"SMS permission granted.")
        }
    }


    private fun showToast(context: Context, message: String) {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}