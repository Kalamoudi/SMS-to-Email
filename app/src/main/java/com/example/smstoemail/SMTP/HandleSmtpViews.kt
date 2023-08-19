package com.example.smstoemail.Smtp

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.Entity.SmtpData
import com.example.smstoemail.Interfaces.recyclerMessageDao
import com.example.smstoemail.Interfaces.smtpDao
import com.example.smstoemail.R
import com.example.smstoemail.Repository.AppDatabase
import com.example.smstoemail.Utils
import com.example.smstoemail.sharedPrefs
import com.example.smstoemail.smtpDataList
import com.example.smstoemail.userEmail
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HandleSmtpViews {


    companion object {
        private var forceStopped: Boolean = false

        fun setForcedStopped(boolValue: Boolean) {
            forceStopped = boolValue
        }
    }


    // TextViews
    private lateinit var SMTPHostText: TextView
    private lateinit var SMTPPortText: TextView
    private lateinit var SMTPUsernameText: TextView
    private lateinit var SMTPPasswordText: TextView


    // EditTexts
    private lateinit var SMTPHostEdit: EditText
    private lateinit var SMTPPortEdit: EditText
    private lateinit var SMTPUsernameEdit: EditText
    private lateinit var SMTPPasswordEdit: EditText
    private lateinit var SMTPSumbitForm: Button

    // ImageViews
    private lateinit var visibilityButtonLayout: RelativeLayout
    private lateinit var visibilityButton: ImageView

    fun handleViews(context: Context){
        var appCompatActivity = context as AppCompatActivity
        // Defining the views

        // TextViews
        SMTPHostText = appCompatActivity.findViewById(R.id.SMTPHostTextView)
        SMTPPortText = appCompatActivity.findViewById(R.id.SMTPPortTextView)
        SMTPUsernameText = appCompatActivity.findViewById(R.id.SMTPUsernameTextView)
        SMTPPasswordText = appCompatActivity.findViewById(R.id.SMTPPasswordTextView)

        // EditTexts
        SMTPHostEdit = appCompatActivity.findViewById(R.id.SMTPHostEditText)
        SMTPPortEdit = appCompatActivity.findViewById(R.id.SMTPPortEditText)
        SMTPUsernameEdit = appCompatActivity.findViewById(R.id.SMTPUsernameEditText)
        SMTPPasswordEdit = appCompatActivity.findViewById(R.id.SMTPPasswordEditText)

        // Buttons
        SMTPSumbitForm = appCompatActivity.findViewById(R.id.SMTPSubmitForm)

        // ImageViews
        visibilityButtonLayout = appCompatActivity.findViewById(R.id.visibilityButtonLayout)
        visibilityButton = appCompatActivity.findViewById(R.id.visibilityButton)


        processVisibilityButton(context)





        if(smtpDataList.isEmpty()){
                sharedPrefs.edit().putBoolean("addingToSmtpDb", true).apply()
            }
            else{
                sharedPrefs.edit().putBoolean("addingToSmtpDb", false).apply()
            }
            if(!sharedPrefs.getBoolean("addingToSmtpDb", true)){
                accessSmtpFromDb(context, smtpDataList)
            }



        SMTPSumbitForm.setOnClickListener {


            when(SMTPSumbitForm.text.toString()){
                "Submit" -> processSubmitForm(context)
                else -> processEditForm(context)

            }

        }


    }

    private fun accessSmtpFromDb(context: Context, smtpDataList: List<SmtpData>){

        val smtpData = smtpDataList[0]

        SMTPHostEdit.setText(smtpData.host)
        SMTPPortEdit.setText(smtpData.port)
        SMTPUsernameEdit.setText(smtpData.username)
        SMTPPasswordEdit.setText(smtpData.password)

        val textColor = Utils.getColorFromAttribute(context, "summaryColor")


        elementsVisibility(context, View.INVISIBLE)
        changeEditTextToTextView(context, R.attr.transparent, false, textColor)
        SMTPSumbitForm.text = "Edit"


    }

    private fun processSubmitForm(context: Context){

       // val textColor = Utils.getColorFromAttribute(context, "textColorPrimary")
        val textColor = Utils.getColorFromAttribute(context, "summaryColor")


        if(!checkedValidInputs(context)){
            return
        }


        val smtpInSmtpData = SmtpData(0, SMTPHostEdit.text.toString(), SMTPPortEdit.text.toString(),
            SMTPUsernameEdit.text.toString(), SMTPPasswordEdit.text.toString())


        if(sharedPrefs.getBoolean("addingToSmtpDb", true)) {
            CoroutineScope(Dispatchers.IO).launch {

             //   val database = AppDatabase.getInstance(context)
             //   smtpDao = database.smtpDao()
                smtpDataList = smtpDao.getAllItems()
                if(!smtpDataList.isEmpty()) {
                    smtpDao.delete(smtpDataList[0])
                }
                Utils.saveNewItem(smtpInSmtpData, smtpDao)
                smtpDataList = smtpDao.getAllItems()
            }
        }

        sharedPrefs.edit().putBoolean("addingSmtpDb", false).apply()

        elementsVisibility(context, View.INVISIBLE)
        changeEditTextToTextView(context, R.attr.transparent, false, textColor)
        SMTPSumbitForm.text = "Edit"
        recreate(context as AppCompatActivity)


    }


    private fun elementsVisibility(context: Context, visibility: Int){

        SMTPPasswordText.visibility = visibility
        SMTPPasswordEdit.visibility = visibility
        visibilityButton.visibility = visibility
      //  visibilityButtonLayout.visibility = visibility

        val smtpAd: AdView = (context as AppCompatActivity).findViewById(R.id.adViewInSmtp)
        smtpAd.visibility = if(visibility==View.INVISIBLE) View.VISIBLE else View.INVISIBLE

    }


    private fun changeEditTextToTextView(context: Context, backgroundColor: Int,
                 enabled: Boolean, textColor: Int = Utils.getColorFromAttribute(context, "textColorPrimary")){


        SMTPHostEdit.isEnabled = enabled
        Utils.setBackgroundTint(context, backgroundColor, SMTPHostEdit)
        SMTPHostEdit.setTextColor(textColor)


        SMTPPortEdit.isEnabled =enabled
        Utils.setBackgroundTint(context, backgroundColor, SMTPPortEdit)
        SMTPPortEdit.setTextColor(textColor)


        SMTPUsernameEdit.isEnabled = enabled
        Utils.setBackgroundTint(context, backgroundColor, SMTPUsernameEdit)
        SMTPUsernameEdit.setTextColor(textColor)


        SMTPPasswordEdit.isEnabled = enabled
        Utils.setBackgroundTint(context, backgroundColor, SMTPUsernameEdit)
        SMTPPasswordEdit.setTextColor(textColor)

    }


    private fun processEditForm(context: Context){


        elementsVisibility(context, View.VISIBLE)
        changeEditTextToTextView(context, android.R.attr.textColorPrimary, true)

        // Reset values to default
        SMTPSumbitForm.text = "Submit"
//        SMTPHostEdit.setText("")
//        SMTPPortEdit.setText("")
//        SMTPUsernameEdit.setText("")
        SMTPPasswordEdit.setText("")

        sharedPrefs.edit().putBoolean("addingToSmtpDb", true).apply()


    }

    private fun checkedValidInputs(context: Context): Boolean{
        var booleanList = arrayOf(false, false, false, false)
        if(!Utils.isValidInput(SMTPHostEdit.text.toString(), "SMTP")) {
            Utils.invalidEditTextInput(context, SMTPHostEdit, SMTPHostEdit.text.toString(), "SMTP")
            booleanList[0] = false
        }else{
            Utils.setBackgroundTint(context, android.R.attr.textColorPrimary, SMTPHostEdit)
            booleanList[0] = true
        }

        if(!Utils.isValidInput(SMTPPortEdit.text.toString(), "Port")){
            Utils.invalidEditTextInput(context, SMTPPortEdit, SMTPPortEdit.text.toString(), "Port")
            booleanList[1] = false
        }
        else{
            Utils.setBackgroundTint(context, android.R.attr.textColorPrimary, SMTPPortEdit)
            booleanList[1] = true
        }
        if(SMTPUsernameEdit.text.toString().isEmpty()){
            Utils.invalidEditTextInput(context, SMTPUsernameEdit, SMTPUsernameEdit.text.toString(), "Username")
            booleanList[2] = false
        }
        else{
            Utils.setBackgroundTint(context, android.R.attr.textColorPrimary, SMTPUsernameEdit)
            booleanList[2] = true
        }
        if(SMTPPasswordEdit.text.toString().isEmpty()){
            Utils.invalidEditTextInput(context, SMTPPasswordEdit, SMTPPasswordEdit.text.toString(), "Password")
            booleanList[3] = false
        }
        else{
            Utils.setBackgroundTint(context, android.R.attr.textColorPrimary, SMTPPasswordEdit)
            booleanList[3] = true
        }

        return booleanList.all { it }
    }

//    private fun processVisibilityButton(context: Context){
//
//        var visibilityOn: Int = R.drawable.visibility_on_40px
//        var visibilityOff: Int = R.drawable.visibility_off_40px
//
//        if(sharedPrefs.getBoolean("isNightMode", true)){
//            visibilityOn = R.drawable.visibility_on_40px_hidden
//            visibilityOff = R.drawable.visibility_off_40px_hidden
//        }
//
//        if(sharedPrefs.getBoolean("passwordVisible", true)){
//            visibilityButton.setImageResource(visibilityOn)
//            SMTPPasswordEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
//        }
//        else{
//            visibilityButton.setImageResource(visibilityOff)
//
//            SMTPPasswordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
//        }
//
//    }

    private fun processVisibilityButton(context: Context){

        var visibilityOn: Int = R.drawable.visibility_on_40px
        var visibilityOff: Int = R.drawable.visibility_off_40px

        if(sharedPrefs.getBoolean("isNightMode", true)){
            visibilityOn = R.drawable.visibility_on_40px_hidden
            visibilityOff = R.drawable.visibility_off_40px_hidden
        }
        SMTPPasswordEdit.setSelection(SMTPPasswordEdit.text.length)

        if(sharedPrefs.getBoolean("passwordVisible", true)){
            visibilityButton.setImageResource(visibilityOn)
            SMTPPasswordEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
        else{
            visibilityButton.setImageResource(visibilityOff)

            SMTPPasswordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
        }


        visibilityButtonLayout.setOnClickListener {
            val isPasswordVisible = SMTPPasswordEdit.transformationMethod is PasswordTransformationMethod

            if (isPasswordVisible) {
                visibilityButton.setImageResource(visibilityOn)
                SMTPPasswordEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
                sharedPrefs.edit().putBoolean("passwordVisible", true).apply()
            } else {
                visibilityButton.setImageResource(visibilityOff)
                SMTPPasswordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
                sharedPrefs.edit().putBoolean("passwordVisible", false).apply()
            }

            // Move the cursor to the end of the text after changing transformation method
            SMTPPasswordEdit.setSelection(SMTPPasswordEdit.text.length)
        }
    }


}