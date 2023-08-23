package com.example.smstoemail.SmsFilters

import android.app.AlertDialog
import android.content.Context
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import com.example.smstoemail.utilsContext

object SmsFilterUtils {



    fun premiumPromptDialog(context: Context){
        val dialogBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialogThemeDark)
            .setTitle("Premium Version")
            .setMessage("Free version only allows one filter, buy premium to add more")
            .setPositiveButton("OK", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
