package com.example.smstoemail.Pages

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import com.example.smstoemail.userEmail

class HandleMainPageViews {


    companion object {
        private var forceStopped: Boolean = false

        fun setForcedStopped(boolValue: Boolean) {
            forceStopped = boolValue
        }
    }

    private lateinit var addEmailText: EditText
    private lateinit var addEmailSubmit: Button
    private lateinit var editEmailButton: Button
    private lateinit var selectedEmail: TextView
    private lateinit var smsTextHeader: TextView
    private lateinit var smsRecyclerView: RecyclerView

    fun handleViews(context: Context){
        var appCompatActivity = context as AppCompatActivity
        // Defining the views


        addEmailText = appCompatActivity.findViewById(R.id.addEmailText)
        addEmailSubmit = appCompatActivity.findViewById(R.id.addEmailSubmit)
        editEmailButton = appCompatActivity.findViewById(R.id.editEmailButton)
        selectedEmail = appCompatActivity.findViewById(R.id.selectedEmail)
        smsTextHeader = appCompatActivity.findViewById(R.id.smsTextHeader)
        smsRecyclerView = appCompatActivity.findViewById(R.id.smsRecyclerView)

        var savedEmail = Utils.retrieveEmailFromSharedPreferences(context)

        if(forceStopped){
            savedEmail = ""
            userEmail = ""
            forceStopped = false
        }
        if(savedEmail === "") {
            addEmail(context)
        }
        else{
            processAddEmail(savedEmail)
            userEmail = savedEmail
            editEmail(context)
        }

    }

    private fun addEmail(context: Context){

        addEmailSubmit.setOnClickListener {
            // Retrieve the email entered by the user

            com.example.smstoemail.userEmail = addEmailText.text.toString()

            // Update UI to display the selected email and hide the EditText and button

            if(Utils.isValidEmail(com.example.smstoemail.userEmail)){
                processAddEmail(com.example.smstoemail.userEmail)
                Utils.saveEmailToSharedPreferences(context, com.example.smstoemail.userEmail)
            }
            else{
                invalidEmailInput(com.example.smstoemail.userEmail)
            }


        }
    }

    private fun editEmail(context: Context){
        editEmailButton.setOnClickListener {
            processEditEmail()
            addEmail(context)

        }
    }
    private fun invalidEmailInput(email: String){
        selectedEmail.text = "$email"
        addEmailText.error = "Incorrect Email Format"
    }

    private fun processAddEmail(email: String) {
        // Display the selected email in a TextView or any other UI element
        selectedEmail.text = "$email"

        // Change visibility of relevant elements
        addEmailText.visibility = View.INVISIBLE
        addEmailSubmit.visibility = View.INVISIBLE
        selectedEmail.visibility = View.VISIBLE
        editEmailButton.visibility = View.VISIBLE
        smsTextHeader.visibility = View.VISIBLE
        smsRecyclerView.visibility = View.VISIBLE
    }

    private fun processEditEmail(){

        // Change visibility of relevant elements
        selectedEmail.visibility = View.INVISIBLE
        addEmailText.visibility = View.VISIBLE
        addEmailSubmit.visibility = View.VISIBLE
        editEmailButton.visibility = View.INVISIBLE
        smsTextHeader.visibility = View.INVISIBLE
        smsRecyclerView.visibility = View.INVISIBLE
        //   addEmailText.setBackgroundResource(R.drawable.edittext_normal_bottom_line)

    }




}