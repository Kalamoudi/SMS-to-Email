package com.kuaapps.smstoemail.ViewMessages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuaapps.smstoemail.GoogleSignIn.SignInWithGmail
import com.kuaapps.smstoemail.MainActivityUtils
import com.kuaapps.smstoemail.NavigationDrawer.HandleNavDrawer
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Utils
import com.kuaapps.smstoemail.Utils.RC_SIGN_IN
import com.kuaapps.smstoemail.smsAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn

class ViewMessagesActivity : AppCompatActivity() {

    private lateinit var handleNavDrawer: HandleNavDrawer
    private lateinit var signInWithGmail: SignInWithGmail
    private lateinit var smsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        MainActivityUtils.initializeTheme(this)
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_view_messages)


        Utils.showAd(this, findViewById(R.id.adViewInViewMessages))

        //========= Tool bar functionalities ================
        // Process and handles navigation drawer logic
        MainActivityUtils.processNavigationDrawer(this)
        handleNavDrawer = HandleNavDrawer(this)
        handleNavDrawer.handleNavDrawer()


        // Activate SignIn with google service
        signInWithGmail = SignInWithGmail()
        signInWithGmail.handleSignIn(this)
        //====================================================



        smsRecyclerView = findViewById(R.id.smsRecyclerViewInViewMessages)
        smsRecyclerView.adapter = smsAdapter
        smsRecyclerView.layoutManager = LinearLayoutManager(this)

        Utils.addBottomMarginForAd(this, findViewById<RelativeLayout>(R.id.viewMessagesPage),
            R.dimen.viewMessagesPageRelativeLayoutMarginBottom)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
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



}
