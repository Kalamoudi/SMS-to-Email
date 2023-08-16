package com.example.smstoemail.ViewMessages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.GoogleSignIn.SignInWithGmail
import com.example.smstoemail.MainActivityUtils
import com.example.smstoemail.NavigationDrawer.HandleNavDrawer
import com.example.smstoemail.R
import com.example.smstoemail.Settings.SettingsFragment
import com.example.smstoemail.Utils
import com.example.smstoemail.Utils.RC_SIGN_IN
import com.example.smstoemail.smsAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn

class ViewMessagesActivity : AppCompatActivity() {

    private lateinit var handleNavDrawer: HandleNavDrawer
    private lateinit var signInWithGmail: SignInWithGmail
    private lateinit var smsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MainActivityUtils.processAppTheme(this)

        setContentView(R.layout.view_messages_activity)

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
