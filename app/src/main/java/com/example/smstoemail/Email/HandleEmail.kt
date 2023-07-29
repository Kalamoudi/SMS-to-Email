package com.example.smstoemail.Email

import android.app.Activity
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
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
        //val host = "smtp.mail.yahoo.com" // Yahoo SMTP server
        val host = "smtp.gmail.com"
        val port = 587 // Yahoo SMTP port
        //val username = "khalid.smssender@yahoo.com" // Replace with your Yahoo email address
        //val password = "Alamoudi1234!" // Replace with your Yahoo email password

        //val host = "mail.smtp2go.com"
        //val port = 2525


        val username = "khalid.smssender@gmail.com" // Replace with your Yahoo email address
        val password = "bkvtmglaxjuslbxe" // Replace with your Yahoo email password

        //val username = "khalid.alamoudi@amoudi.us"
        //val password = "mustadim"

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