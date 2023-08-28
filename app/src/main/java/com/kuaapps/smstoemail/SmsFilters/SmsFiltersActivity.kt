package com.kuaapps.smstoemail.SmsFilters
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.kuaapps.smstoemail.Interfaces.smsFilterRecyclerMessageDao
import com.kuaapps.smstoemail.MainActivityUtils
import com.kuaapps.smstoemail.NavigationDrawer.HandleNavDrawer
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Repository.AppDatabase
import com.kuaapps.smstoemail.Utils
import com.kuaapps.smstoemail.signInWithGmail
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.kuaapps.smstoemail.smsFilterAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager


class SmsFiltersActivity  : AppCompatActivity(){

    private lateinit var handleNavDrawer: HandleNavDrawer
    private val filters = mutableListOf<String>()
    private lateinit var smsFilterRecyclerView: RecyclerView

    private lateinit var smsFilterGridLayout: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        MainActivityUtils.initializeTheme(this)
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_sms_filters)

//        Utils.showAd(this, findViewById(R.id.adViewInSmsFilter))
//        Utils.addBottomMarginForAd(this, findViewById<NestedScrollView>(R.id.smsFilterPage),
//            R.dimen.viewMessagesPageRelativeLayoutMarginBottom)

        //========= Tool bar functionalities ================
        // Process and handles navigation drawer logic
        MainActivityUtils.processNavigationDrawer(this)
        handleNavDrawer = HandleNavDrawer(this)
        handleNavDrawer.handleNavDrawer()


        // Activate SignIn with google service
        // signInWithGmail = SignInWithGmail()
        signInWithGmail.handleSignIn(this)
        //====================================================



        smsFilterRecyclerView = findViewById(R.id.smsFiltersRecyclerView)
        smsFilterRecyclerView.adapter = smsFilterAdapter
      //  smsFilterRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        smsFilterRecyclerView.layoutManager = flexboxLayoutManager





        // Handle user input for adding filters
        val addButton = findViewById<Button>(R.id.smsFiltersAddButton)
        val filterEditText = findViewById<EditText>(R.id.smsFiltersEditText)

        // Pressing Enter will automatically perform add button
        filterEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                // Simulate button click
                addButton.performClick()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        addButton.setOnClickListener {
            var newFilter = filterEditText.text.toString()
            newFilter = newFilter.trim()
            if (newFilter.isNotEmpty()) {
                smsFilterAdapter.addSmsFilter(this, SmsFilter(newFilter))
                filterEditText.text.clear()
            }
        }

        val clearAllFilters: Button = findViewById(R.id.smsFilterClearAllFiltersButton)

        clearAllFilters.setOnClickListener {
            smsFilterAdapter.clearAllFilters()

        }





    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Utils.RC_SIGN_IN) {
            signInWithGmail.handleGoogleSignInResult(this, GoogleSignIn.getSignedInAccountFromIntent(data))
            recreate()
        }

        if (requestCode == Utils.REQUEST_AUTHORIZATION) {
            // Handle the result of user consent activity
            if (resultCode == Activity.RESULT_OK) {
                // User granted consent, you can now proceed with the operation
            } else {
                // User did not grant consent, handle as needed
            }
        }
    }


    private fun initializeSmsFilterRecyclerMessagesDao(context: Context){
        GlobalScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getInstance(context)
            smsFilterRecyclerMessageDao = database.smsFilterRecyclerMessageDao()
            val smsFilterRecyclerMessages = smsFilterRecyclerMessageDao.getAllItems()
            smsFilterAdapter.updateSmsFilterList(smsFilterRecyclerMessages)
            // Use the 'items' in the UI if needed (e.g., update the UI with the data)
        }
    }


}