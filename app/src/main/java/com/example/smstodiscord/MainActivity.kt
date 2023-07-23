package com.example.smstodiscord

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
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
            val emailSender = "your_email@gmail.com" // Replace with your email address
            val recipient = "khalamoudi91@gmail.com"
            val subject = "New SMS from $sender"
            val body = "SMS Body: $messageBody"

            // Call the sendEmail function with all required arguments
            sendEmail(emailSender, recipient, subject, body)
        } else {
            // Handle the case when sender or messageBody is null (e.g., show an error message)
        }
    }

    private fun sendEmail(from: String, to: String, subject: String, body: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Set properties for the mail server
                val props = Properties()
                props["mail.smtp.auth"] = "true"
                props["mail.smtp.starttls.enable"] = "true"
                props["mail.smtp.host"] = "smtp.gmail.com" // Use your SMTP server here, e.g., smtp.example.com
                props["mail.smtp.port"] = "587" // Use the appropriate port for your SMTP server

                // Provide your email account credentials here
                val username = "khalid.smssender@gmail.com"
                val password = "Khalid1234!"

                // Create a mail session with the properties
                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(username, password)
                    }
                })

                // Create the email message
                val message = MimeMessage(session)
                message.setFrom(InternetAddress(from))
                message.setRecipient(Message.RecipientType.TO, InternetAddress(to))
                message.subject = subject
                message.setText(body)

                // Send the email
                Transport.send(message)
                runOnUiThread {
                    showToast("Email sent successfully!")
                }
            } catch (e: MessagingException) {
                e.printStackTrace()
                runOnUiThread {
                    showToast("Error sending email: ${e.message}")
                }
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun addSmsToRecyclerView(sender: String, message: String) {
        smsAdapter.addSms(SmsData(sender, message))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }
}

