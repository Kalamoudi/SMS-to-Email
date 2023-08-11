package com.example.smstoemail.Email

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.smstoemail.sharedPrefs
import com.example.smstoemail.smtpDataList
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class HandleEmail {




    fun sendEmail(context: Context, recipientEmail: String, mailSubject: String, mailBody: String) {

        var host = "smtp.gmail.com"
        var port = 587
        var username = "khalid.smssender@gmail.com" // Replace with your Yahoo email address
        var password = "bkvtmglaxjuslbxe" // Replace with your Yahoo email password

        if(!smtpDataList.isEmpty() && sharedPrefs.getBoolean("useSmtp", true)){
            host = smtpDataList[0].host
            port = smtpDataList[0].port.toInt()
            username = smtpDataList[0].username
            password = smtpDataList[0].password
        }

        val account = GoogleSignIn.getLastSignedInAccount(context)

        if (account != null) {
            GlobalScope.launch(Dispatchers.Main) {
                SendWithGoogleMail.sendEmailUsingGmailAPI(context, account, recipientEmail, mailSubject, mailBody)
            }
            return
        }

//        Log.d("Host", host)
//        Log.d("Port", port.toString())
//        Log.d("Username", username)
//        Log.d("Password", password)
        //val host = "smtp.mail.yahoo.com" // Yahoo SMTP server
//        val host = "smtp.gmail.com"
//        val port = 587 // Yahoo SMTP port
        //val username = "khalid.smssender@yahoo.com" // Replace with your Yahoo email address

//        host = "mail.smtp2go.com"
//        port = 2525
//        username = "khalid.alamoudi@amoudi.us"



//        val username = "khalid.smssender@gmail.com" // Replace with your Yahoo email address
//        val password = "bkvtmglaxjuslbxe" // Replace with your Yahoo email password


        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = port

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        // Use the MainScope to launch the coroutine on the main thread
        val mainScope = MainScope()

        mainScope.launch {
            try {
                val message = MimeMessage(session)
                message.setFrom(InternetAddress(username))
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)) // Set the recipient's email address
                message.subject = mailSubject
                message.setText(mailBody)

                withContext(Dispatchers.IO) {
                    // Perform the network operation (sending the email)
                    Transport.send(message)
                }
                showToast(context,"Email sent successfully!")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(context,"Error sending email: ${e.message}")
            } finally {
                // Cancel the MainScope to avoid leaks
                mainScope.cancel()
            }
        }
    }


    private fun showToast(context: Context, message: String) {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}