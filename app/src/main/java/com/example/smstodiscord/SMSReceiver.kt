package com.example.smstodiscord

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Message
import java.io.ByteArrayOutputStream
import java.util.Collections
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.Session
import javax.mail.Message.RecipientType
import android.util.Base64
import com.google.android.gms.common.api.Scope

class SMSReceiver : BroadcastReceiver() {

    private var smsListener: SmsListener? = null

    interface SmsListener {
        fun onSmsReceived(sender: String?, messageBody: String?)
    }

    fun setSmsListener(listener: SmsListener) {
        smsListener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            // Handle SMS messages
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<Any>?
                if (pdus != null) {
                    for (pdu in pdus) {
                        val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                        val sender = smsMessage.displayOriginatingAddress
                        val messageBody = smsMessage.messageBody

                        // Notify the listener that an SMS has been received
                        smsListener?.onSmsReceived(sender, messageBody)
                    }
                }
            }
        } else if (intent.action == "android.provider.Telephony.RCS_MESSAGE_RECEIVED") {
            // Handle RCS messages
            // Retrieve the RCS message data (sender and message body) from the intent extras
            val sender = intent.getStringExtra("sender")
            val messageBody = intent.getStringExtra("message_body")

            // Notify the listener that an RCS message has been received
            smsListener?.onSmsReceived(sender, messageBody)
        }
    }
}

