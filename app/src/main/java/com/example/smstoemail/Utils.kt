package com.example.smstoemail


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.Interfaces.BaseDao
import com.example.smstoemail.Interfaces.RecyclerMessageDao
import com.example.smstoemail.Pages.HandleMainPageViews
import com.example.smstoemail.Repository.AppDatabase
import com.example.smstoemail.Services.BackgroundService
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress

public lateinit var utilsContext: Context
public var userEmail = ""
public var isNightMode: Boolean = false
public var currentAppTheme = R.style.AppTheme_Dark

public lateinit var RecyclerMessageDao: RecyclerMessageDao

object TableNames {
    const val RECYCLER_MESSAGE_TABLE = "RecyclerMessage"
}


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
        // Closes the background service
        val serviceIntent = Intent(context, BackgroundService::class.java)
        context.stopService(serviceIntent)
        // Closes the app
        (context as Activity).finish()

    }

    fun setAppTheme(nightMode: Boolean) {
        isNightMode = nightMode
    }

    fun isNightMode(): Boolean {
        return isNightMode
    }


    fun setBackgroundTint(context: Context, myEditText: EditText?, attribute: Int) {
        // Get the color from the theme attribute
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attribute, typedValue, true)

        // Set the underline color programmatically using the resolved color
        myEditText?.backgroundTintList = AppCompatResources.getColorStateList(context, typedValue.resourceId)
    }

    fun setTheme(context: Context, xmlId: Int, attribute: Int){

        val navigationView: NavigationView = (context as AppCompatActivity).findViewById(xmlId)
        navigationView.context.theme.applyStyle(attribute, true)

        Log.d("Theme", "    e successfully: $attribute")
    }

    fun checkSharedPreference(context: Context, reason: String){
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean(reason, true)) {
            HandleMainPageViews.setForcedStopped(false)
            val editor = sharedPreferences.edit()
            editor.remove(reason)
            editor.apply()
        }

    }

    // saves recyclerMessage to Database
    suspend fun saveNewItem(recyclerMessage: RecyclerMessage) {
        // Perform the database operation using a coroutine on a background thread
        withContext(Dispatchers.IO) {
            val itemDao = RecyclerMessageDao
            itemDao.insert(recyclerMessage)
        }
    }

    suspend fun <T> saveNewItem(item: T, itemDao: BaseDao<T>) {
        // Perform the database operation using a coroutine on a background thread
        withContext(Dispatchers.IO) {
            val itemDao = itemDao
            itemDao.insert(item)
        }
    }

    fun setIsNightMode(context: Context){
        val nightMode: Boolean = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
            .getBoolean("settingsThemeSwitch", true)

        isNightMode = nightMode

    }

    fun getCurrentThemeAsInt(): Int{
        if(isNightMode){
            return R.style.AppTheme_Dark
        }
        return R.style.AppTheme
    }

}