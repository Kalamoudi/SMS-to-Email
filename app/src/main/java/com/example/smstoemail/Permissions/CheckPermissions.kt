package com.example.smstoemail.Permissions

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.example.smstoemail.Services.BackgroundService
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.smstoemail.MainActivity
import com.example.smstoemail.Sms.SMSAdapter
import com.example.smstoemail.Utils


class CheckPermissions {


    private var permissionsMap: MutableMap<String, MutableList<String>> = Utils.getPermissionsMap().toMutableMap()



    fun handlePermissions(context: Context){


        requestPermissions(context)

    }

    private fun requestPermissions(context: Context) {

        ActivityCompat.requestPermissions(context as Activity, Utils.permissionsList, Utils.REQUEST_CODE_PERMISSIONS)
//        if (!allPermissionsGranted(context)) {
//            ActivityCompat.requestPermissions(context as Activity, permissionsList, REQUEST_CODE_PERMISSIONS)
//        } else {
////            Utils.showToast(context, "SMS: " + permissionsMap[Manifest.permission.RECEIVE_SMS] +
////                    "\nPhone State" + permissionsMap[Manifest.permission.READ_PHONE_STATE] +
////                    "\nPhone Numbers" + permissionsMap[Manifest.permission.READ_PHONE_NUMBERS])
//            Utils.showToast(context, "All permissions Granted")
//        }
    }

    public fun allPermissionsGranted(context: Context): Boolean {
        for (permissionEntry in permissionsMap) {
            if (ContextCompat.checkSelfPermission(context, permissionEntry.key) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            else{
                permissionsMap[permissionEntry.key]?.set(1, "Granted")
            }
        }
        return true
    }


    fun handlePermissionsResult(
        context: Context,
        requestCode: Int,
        permissionsList: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Utils.REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
               // Utils.showToast(context, "All permissions Granted")
            } else {
                //requestPermissions(context)
                showPermissionRationale(context)
            }
        }
    }


    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.example.smstoemail", null)
        intent.data = uri
        try {
            startActivityForResult(context as Activity, intent, Utils.PERMISSION_SETTINGS_REQUEST_CODE, null)
        } catch (e: ActivityNotFoundException) {
            // Handle the case when the Settings activity is not found
            Utils.closeAppCompletely(context) // Close the app if the Settings activity is not available
        }
    }

    private fun showPermissionRationale(context: Context) {
        // Show a dialog explaining the need for the permission
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Permissions Required")
        dialogBuilder.setMessage("Permissions required:\n -"
                + permissionsMap[Utils.permissionsList[0]]?.get(0) +"\n -"
                + permissionsMap[Utils.permissionsList[1]]?.get(0) + "\n\n" +
                "Optional(to display your phone number):\n -"
                + permissionsMap[Utils.permissionsList[2]]?.get(0))
        dialogBuilder.setPositiveButton("Grant Permission") { _, _ ->
            openAppSettings(context) // Redirect to the app settings
            Utils.closeAppCompletely(context)
        }
        dialogBuilder.setNegativeButton("Cancel") { _, _ ->
            // Handle the case when the user cancels the action
            Utils.closeAppCompletely(context)
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }


}