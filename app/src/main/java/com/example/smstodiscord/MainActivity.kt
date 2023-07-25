package com.example.smstodiscord

import kotlinx.coroutines.*
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smstodiscord.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import kotlinx.coroutines.CoroutineScope
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.mail.Message.RecipientType


class MainActivity : AppCompatActivity(), SMSReceiver.SmsListener {

    private val SMS_PERMISSION_REQUEST_CODE = 101
    private lateinit var smsRecyclerView: RecyclerView
    private lateinit var smsAdapter: SMSAdapter

    private val smsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent?.action != null && intent.action == "android.provider.Telephony.SMS_RECEIVED") {
                val bundle = intent.extras
                if (bundle != null) {
                    val pdus = bundle.get("pdus") as Array<Any>?
                    if (pdus != null) {
                        for (pdu in pdus) {
                            val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                            val sender = smsMessage.displayOriginatingAddress
                            val messageBody = smsMessage.messageBody

                            // Add the received SMS to the RecyclerView
                           // addSmsToRecyclerView(sender, messageBody)

                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)

        smsRecyclerView = findViewById(R.id.smsRecyclerView)
        smsAdapter = SMSAdapter()
        smsRecyclerView.adapter = smsAdapter
        smsRecyclerView.layoutManager = LinearLayoutManager(this)

        checkSmsPermission()

        // Register the SMS receiver
        val smsReceiver = SMSReceiver()
        smsReceiver.setSmsListener(this) // This will work now since MainActivity implements SmsListener
        val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsReceiver, filter)



    }

    override fun onSmsReceived(sender: String?, messageBody: String?) {
        if (sender != null && messageBody != null) {
            // Add the received SMS or RCS message to the RecyclerView
            addSmsToRecyclerView(sender, messageBody)

            // Send email with the message content
            val emailSender = "khalid.smssender@mailsac.com" // Replace with your email address
            val recipient = "khalamoudi91@gmail.com"
            val subject = "SMS from $sender"
            val body = "$messageBody"

            // Call the sendEmail function with all required arguments
            sendEmail("khalamoudi91@gmail.com", subject, body)
        } else {
            // Handle the case when sender or messageBody is null (e.g., show an error message)
        }
    }

    private fun sendEmail(recipientEmail: String, mailSubject: String, mailBody: String) {
        //val host = "smtp.mail.yahoo.com" // Yahoo SMTP server
        val host = "smtp.gmail.com"
        val port = 587 // Yahoo SMTP port
        //val username = "khalid.smssender@yahoo.com" // Replace with your Yahoo email address
        //val password = "Alamoudi1234!" // Replace with your Yahoo email password

        val username = "khalid.smssender@gmail.com" // Replace with your Yahoo email address
        val password = "bkvtmglaxjuslbxe" // Replace with your Yahoo email password
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
                showToast("Email sent successfully!")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Error sending email: ${e.message}")
            } finally {
                // Cancel the MainScope to avoid leaks
                mainScope.cancel()
            }
        }
    }







    private fun checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS),
                SMS_PERMISSION_REQUEST_CODE
            )
        } else {
            showToast("SMS permission granted.")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("SMS permission granted.")
            } else {
                showToast("SMS permission denied.")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addSmsToRecyclerView(sender: String, message: String) {
        smsAdapter.addSms(SmsData(sender, message))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }
}

