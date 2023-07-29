package com.example.smstoemail.Sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Credentials
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.telephony.PhoneStateListener
import android.telephony.SmsMessage
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.example.smstoemail.Email.HandleEmail
import com.example.smstoemail.Utils
import com.example.smstoemail.userEmail
import com.google.android.gms.auth.api.credentials.HintRequest


class SMSReceiver : BroadcastReceiver() {

    //private var smsListener: SmsListener? = null
    private lateinit var handleEmail : HandleEmail

    companion object {
        private var smsAdapter: SMSAdapter? = null

        fun setSMSAdapter(adapter: SMSAdapter) {
            smsAdapter = adapter
        }
    }



    override fun onReceive(context: Context, intent: Intent) {
        val recipient = getPhoneNumber(context)
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
                        onSmsReceived(context, sender, recipient, messageBody)
                    }
                }
            }
        } else if (intent.action == "android.provider.Telephony.RCS_MESSAGE_RECEIVED") {
            // Handle RCS messages
            // Retrieve the RCS message data (sender and message body) from the intent extras
            val sender = intent.getStringExtra("sender")
            val messageBody = intent.getStringExtra("message_body")

            // Notify the listener that an RCS message has been received
            onSmsReceived(context, sender, recipient, messageBody)
        }
    }

    fun onSmsReceived(context: Context, sender: String?, recipient: String, messageBody: String?) {
        if (sender != null && messageBody != null) {
            // Add the received SMS or RCS message to the RecyclerView
            if(Utils.isValidEmail(userEmail)) {
                addSmsToRecyclerView(sender, recipient, messageBody)
            }

            // Send email with the message content
            val emailSender = "khalid.smssender@mailsac.com" // Replace with your email address
            val recipient = "khalamoudi91@gmail.com"
            val subject = "SMS from $sender to $recipient"
            val body = "$messageBody"

            // Call the sendEmail function with all required arguments
            if(Utils.isValidEmail(userEmail)) {
                handleEmail = HandleEmail()
                handleEmail.sendEmail(context, userEmail, subject, body)
            }
        } else {
            // Handle the case when sender or messageBody is null (e.g., show an error message)
        }
    }

    private fun addSmsToRecyclerView(sender: String, recipient: String, message: String) {
        smsAdapter?.addSms(SmsData(sender, recipient, message))
    }


    // Function to get the phone number
    fun getPhoneNumber(context: Context): String {

        //val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // Get a list of active SIM cards (subscriptions)
            val activeSubscriptions: List<SubscriptionInfo> = subscriptionManager.activeSubscriptionInfoList ?: emptyList()

            for (subscriptionInfo in activeSubscriptions) {
                // Check if the phone number is available for each subscription
                val phoneNumber = subscriptionInfo.number
                if (!phoneNumber.isNullOrBlank()) {
                    return phoneNumber
                }
            }
        }

        // If no phone number is available, return null or handle the case accordingly
        return ""
    }

}

