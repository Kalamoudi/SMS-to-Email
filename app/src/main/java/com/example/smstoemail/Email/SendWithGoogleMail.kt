package com.example.smstoemail.Email

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.smstoemail.Utils
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory

import com.google.api.services.gmail.Gmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

import javax.mail.Message.RecipientType
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Message
import org.apache.commons.codec.binary.Base64


object SendWithGoogleMail {

    private const val REQUEST_AUTHORIZATION = 1001


    fun sendEmailUsingGmailAPI(context: Context, account: GoogleSignInAccount?, recipientEmail: String, mailSubject: String, mailBody: String) {

        try {
            val userGmail = account?.email
            val credentials = GoogleAccountCredential.usingOAuth2(
                context, setOf("https://www.googleapis.com/auth/gmail.compose")
            )
            credentials.selectedAccount = account?.account

            val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
            val jsonFactory = JacksonFactory.getDefaultInstance()

            val service = Gmail.Builder(httpTransport, jsonFactory, credentials)
                .setApplicationName("SMS To Email").build()

            val emailContent = createEmail(userGmail!!, recipientEmail, mailSubject, mailBody)


            CoroutineScope(Dispatchers.IO).launch {
                sendMessage(service, "me", emailContent)
            }
        }catch (e: UserRecoverableAuthIOException) {
            // Handle user recoverable auth exception by launching the intent
            (context as Activity).startActivityForResult(e.intent, REQUEST_AUTHORIZATION)
        }



    }

    private fun createEmail(sender: String, to: String, subject: String, body: String): MimeMessage {
        val props = Properties()
        val session = Session.getDefaultInstance(props, null)
        val email = MimeMessage(session)
        email.setFrom(InternetAddress(sender))
        email.addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(to))
        email.subject = subject
        email.setText(body)
        return email
    }

    private fun sendMessage(service: Gmail, userId: String, email: MimeMessage) {
        val buffer = ByteArrayOutputStream()
        email.writeTo(buffer)
        val encodedEmail = Base64.encodeBase64URLSafeString(buffer.toByteArray())
        val message = com.google.api.services.gmail.model.Message()
        message.raw = encodedEmail
        service.users().messages().send(userId, message).execute()
    }



//    private fun requestGmailPermission(context: Context){
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .requestScopes(Scope("https://www.googleapis.com/auth/gmail.compose"))
//            .build()
//
//        val mGoogleApiClient = GoogleApiClient.Builder(context)
//            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//            .build()
//
//
//
//    }

}