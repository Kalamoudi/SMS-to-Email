package com.example.smstoemail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.Permissions.CheckPermissions
import com.example.smstoemail.Sms.HandleSMS


// Java imports

// Kotlin imports

public var userEmail = "";
class MainActivity : AppCompatActivity(){


    private lateinit var checkPermissions: CheckPermissions
    private lateinit var handleSMS: HandleSMS


    private lateinit var addEmailText: EditText
    private lateinit var addEmailSubmit: Button
    private lateinit var editEmailButton: Button
    private lateinit var selectedEmail: TextView
    private lateinit var smsTextHeader: TextView
    private lateinit var smsRecyclerView: RecyclerView


    // Google Sign-in variables
    //private lateinit var googleSignInClient: GoogleSignInClient
    //private val RC_SIGN_IN = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)




        // Checks for permissions
        checkPermissions = CheckPermissions()
        checkPermissions.checkSmsPermission(this)

        // HandlesSMS Receive and sending of Email
        handleSMS = HandleSMS()
        handleSMS.handleSMS(this)


        // Code that Handles
        addEmailText = findViewById(R.id.addEmailText)
        addEmailSubmit = findViewById(R.id.addEmailSubmit)
        //editEmailText = findViewById(R.id.editEmailText)
        editEmailButton = findViewById(R.id.editEmailButton)
        selectedEmail = findViewById(R.id.selectedEmail)
        smsTextHeader = findViewById(R.id.smsTextHeader)
        smsRecyclerView = findViewById(R.id.smsRecyclerView)

        addEmailSubmit.setOnClickListener {
            // Retrieve the email entered by the user
            userEmail = addEmailText.text.toString()

            // Update UI to display the selected email and hide the EditText and button
            showSelectedEmail(userEmail)
        }

        editEmailButton.setOnClickListener {

            editSelectedEmail();

        }




//

    }

    private fun showSelectedEmail(email: String) {
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

    private fun editSelectedEmail(){

        // Change visibility of relevant elements
        selectedEmail.visibility = View.INVISIBLE
        addEmailText.visibility = View.VISIBLE
        addEmailSubmit.visibility = View.VISIBLE
        editEmailButton.visibility = View.INVISIBLE
        smsTextHeader.visibility = View.INVISIBLE
        smsRecyclerView.visibility = View.INVISIBLE

    }

}

