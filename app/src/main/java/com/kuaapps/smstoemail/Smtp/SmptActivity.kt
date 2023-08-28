package com.kuaapps.smstoemail.Smtp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.kuaapps.smstoemail.MainActivityUtils
import com.kuaapps.smstoemail.NavigationDrawer.HandleNavDrawer
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Utils
import com.kuaapps.smstoemail.signInWithGmail
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SmtpActivity : AppCompatActivity(){

    private lateinit var handleNavDrawer: HandleNavDrawer
    private lateinit var handleSMTPViews: HandleSmtpViews

    override fun onCreate(savedInstanceState: Bundle?) {
        MainActivityUtils.initializeTheme(this)
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_smtp)

        //SmtpUtils.addAdvertisement(this)
        Utils.showAd(this, findViewById(R.id.adViewInSmtp))

        Utils.addBottomMarginForAd(this, findViewById<NestedScrollView>(R.id.smtpPage),
            R.dimen.viewMessagesPageRelativeLayoutMarginBottom)

        //========= Tool bar functionalities ================
        // Process and handles navigation drawer logic
        MainActivityUtils.processNavigationDrawer(this)
        handleNavDrawer = HandleNavDrawer(this)
        handleNavDrawer.handleNavDrawer()


        // Activate SignIn with google service
       // signInWithGmail = SignInWithGmail()
        signInWithGmail.handleSignIn(this)
        //====================================================


        handleSMTPViews = HandleSmtpViews()
        handleSMTPViews.handleViews(this)

        Utils.addBottomMarginForAd(this, findViewById<NestedScrollView>(R.id.smtpPage),
            R.dimen.viewMessagesPageRelativeLayoutMarginBottom)

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


}