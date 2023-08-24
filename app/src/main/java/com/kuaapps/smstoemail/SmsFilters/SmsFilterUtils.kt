package com.kuaapps.smstoemail.SmsFilters

import android.app.AlertDialog
import android.content.Context
import com.kuaapps.smstoemail.R

object SmsFilterUtils {



    fun premiumPromptDialog(context: Context){
        val dialogBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialogThemeDark)
            .setTitle("Free Version")
            .setMessage("The free version allows only 2 filters. Purchase the premium version of the app to add more.")
            .setPositiveButton("OK", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    fun removeSpaces(inputString: String): String {
        val strippedString = inputString.trim()
        return if (strippedString.isNotBlank()) {
            strippedString
        } else {
            ""
        }
    }


}
