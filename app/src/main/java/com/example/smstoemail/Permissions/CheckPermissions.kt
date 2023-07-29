package com.example.smstoemail.Permissions

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
    private val READ_PHONE_STATE_CODE = 102
    private val READ_PHONE_NUMBERS_CODE = 103
    private val permissionsMap = mutableMapOf<Int, String>()
    private var permissionsString = ""


    fun handlePermissions(context: Context){


        checkSmsPermission(context)
        checkReadPhoneStatePermission(context)
        checkReadPhoneNumbersPermission(context)


        for(entry in permissionsMap.entries){
            val key = entry.key
            val value = entry.value

            permissionsString += entry.value + ". "

        }

        showToast(context, "Permissions for " + permissionsString + "Granted")





    }


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
            //showToast(context,"SMS permission granted.")
            permissionsMap.put(SMS_PERMISSION_REQUEST_CODE, "SMS")
        }
    }

    fun checkReadPhoneStatePermission(context: Context) {
    // Check if READ_PHONE_STATE permission is granted
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (context is Activity) {
                    // Request the permission
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.READ_PHONE_STATE),
                        READ_PHONE_STATE_CODE
                    )
                }
            } else {
                // Permission is already granted, you can use it here
                //showToast(context,"Phone Read permission granted.")
                permissionsMap.put(READ_PHONE_STATE_CODE, "Phone Read")
            }
    }

    fun checkReadPhoneNumbersPermission(context: Context) {
        // Check if READ_PHONE_STATE permission is granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (context is Activity) {
                // Request the permission
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.READ_PHONE_NUMBERS),
                    READ_PHONE_NUMBERS_CODE
                )
            }
        } else {
            // Permission is already granted, you can use it here
            //showToast(context,"Phone Read permission granted.")
            permissionsMap.put(READ_PHONE_NUMBERS_CODE, "Phone Numbers")
        }
    }


    private fun showToast(context: Context, message: String) {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}