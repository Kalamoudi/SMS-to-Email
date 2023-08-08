package com.example.smstoemail.SMTP

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smstoemail.MainActivityUtils
import com.example.smstoemail.NavigationDrawer.HandleNavDrawer
import com.example.smstoemail.R
import com.example.smstoemail.sharedPrefs
import com.example.smstoemail.utilsContext

class SMPTActivity :AppCompatActivity(){

    private lateinit var handleNavDrawer: HandleNavDrawer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        MainActivityUtils.processAppTheme(this)

        setContentView(R.layout.activity_smtp)

        MainActivityUtils.processNavigationDrawer(this)

        handleNavDrawer = HandleNavDrawer(this)
        handleNavDrawer.handleNavDrawer()



    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }

}