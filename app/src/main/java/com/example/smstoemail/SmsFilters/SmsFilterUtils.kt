package com.example.smstoemail.SmsFilters

import android.app.AlertDialog
import android.content.Context
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import com.example.smstoemail.sharedPrefs
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
