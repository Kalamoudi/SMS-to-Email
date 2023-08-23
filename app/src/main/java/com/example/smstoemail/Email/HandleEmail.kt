package com.example.smstoemail.Email

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.toLowerCase
import com.example.smstoemail.Interfaces.smsFilterRecyclerMessageDao
import com.example.smstoemail.Smtp.SmtpUtils
import com.example.smstoemail.sharedPrefs
import com.example.smstoemail.smsAdapter
import com.example.smstoemail.smsFilterAdapter
import com.example.smstoemail.smtpDataList
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
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


    fun handleSendEmail(context: Context, recipientEmail: String, mailSubject: String, mailBody: String){

//        var smtpData = mutableMapOf<String, String>(
//            "host" to "smtp.gmail.com",
//            "port" to "587",
//            "username" to "smstoemail.smssender@gmail.com",
//            "password" to "sztwydqlysvcuinm"
//        )

        var sendMessage: Boolean = true
        val smsFilterList = smsFilterAdapter.getSmsFilterList()
        if(smsFilterList.isNotEmpty()){
            sendMessage = false
            for(smsFilter in smsFilterList){
                if(mailBody.lowercase().contains(smsFilter.filter)){
                    sendMessage = true
                }
            }
        }


        if(!sendMessage){
            return
        }



        if(smtpDataList.isNotEmpty() && sharedPrefs.getBoolean("useSmtp", true)){
            var smtpData = mutableMapOf<String, String>()
            val decryptedSmtpDataList = SmtpUtils.decryptSmtpData(smtpDataList)
            smtpData["host"] = decryptedSmtpDataList.host
            smtpData["port"] = decryptedSmtpDataList.port
            smtpData["username"] = decryptedSmtpDataList.username
            smtpData["password"] = decryptedSmtpDataList.password
            sendEmail(context, recipientEmail, mailSubject, mailBody, smtpData)
        }

        val account = GoogleSignIn.getLastSignedInAccount(context)

        if (account != null && sharedPrefs.getBoolean("useGmail", true)) {
            GlobalScope.launch(Dispatchers.Main) {
                SendWithGoogleMail.sendEmailUsingGmailAPI(context, account, recipientEmail, mailSubject, mailBody)
            }
        }



       // sendEmail(context, recipientEmail, mailSubject, mailBody, smtpData)



    }

    fun sendEmail(context: Context, recipientEmail: String, mailSubject: String, mailBody: String, smtpData: MutableMap<String, String>) {


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
        props["mail.smtp.host"] = smtpData["host"]
        props["mail.smtp.port"] = smtpData["port"]

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(smtpData["username"], smtpData["password"])
            }
        })

        // Use the MainScope to launch the coroutine on the main thread
        val mainScope = MainScope()

        mainScope.launch {
            try {
                val message = MimeMessage(session)
                message.setFrom(InternetAddress(smtpData["username"]))
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