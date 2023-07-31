package com.example.smstoemail


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import com.example.smstoemail.Services.BackgroundService
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress


public var userEmail = "";


object Utils {

    public const val REQUEST_CODE_PERMISSIONS = 101
    public const val PERMISSION_SETTINGS_REQUEST_CODE = 102
    val permissionsList: Array<String> = getPermissionsMap().keys.toTypedArray()

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

        return emailRegex.matches(email)

    }

    fun saveEmailToSharedPreferences(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.apply()
    }

    fun retrieveEmailFromSharedPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_email", "") ?: ""
    }

    fun getPermissionsMap(): Map<String, MutableList<String>>{
        val permissionsMap: Map<String, MutableList<String>> =
            mapOf(
            Manifest.permission.RECEIVE_SMS to mutableListOf("SMS", "Denied"),
            Manifest.permission.READ_PHONE_STATE to mutableListOf("Phone", "Denied"),
            Manifest.permission.READ_PHONE_NUMBERS to mutableListOf("Contacts", "Denied")
            )
        return permissionsMap
    }

    fun showToast(context: Context, message: String) {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun closeAppCompletely(context: Context){
        val serviceIntent = Intent(context, BackgroundService::class.java)
        context.stopService(serviceIntent)
        (context as Activity).finish()

    }

}