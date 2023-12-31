package com.kuaapps.smstoemail.Sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import com.kuaapps.smstoemail.Email.HandleEmail
import com.kuaapps.smstoemail.Utils
import com.kuaapps.smstoemail.sharedPrefs
import com.kuaapps.smstoemail.userEmail
import java.util.Calendar


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
        var smsStrings: SmsStrings

        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            // Handle SMS messages
            val bundle = intent.extras

            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<Any>?
                if (pdus != null) {
                    // Check if the message is part of a long SMS
                    smsStrings = reassembleLongCheckedSms(context, recipient, pdus)
                    onSmsReceived(context, smsStrings)
                }
            }
        } else if (intent.action == "android.provider.Telephony.RCS_MESSAGE_RECEIVED") {
            // Handle RCS messages
            // Retrieve the RCS message data (sender and message body) from the intent extras
            val sender = intent.getStringExtra("sender")
            val messageBody = intent.getStringExtra("message_body")


            // Notify the listener that an RCS message has been received
            smsStrings = SmsStrings(sender!!, recipient, messageBody!!, Calendar.getInstance())
            onSmsReceived(context, smsStrings)
        }
    }


    fun onSmsReceived(context: Context, smsStrings: SmsStrings) {
        if (smsStrings.sender != null && smsStrings.messageBody != null) {
            // Add the received SMS or RCS message to the RecyclerView
            if(Utils.isValidEmail(userEmail)) {
                addSmsToRecyclerView(smsStrings)
            }

            // Send email with the message content
            val emailSender = "khalid.smssender@mailsac.com" // Replace with your email address
            val recipient = "khalamoudi91@gmail.com"
            val subject = "SMS from " + smsStrings.sender + " to " + smsStrings.recipient
            val body = smsStrings.messageBody

            // Call the sendEmail function with all required arguments
            if(Utils.isValidEmail(userEmail)) {
                if(sharedPrefs.getBoolean("wifiOnly", true) and !Utils.checkIfWifiIsOn(context)){
                    return
                }
                handleEmail = HandleEmail()
                handleEmail.handleSendEmail(context, userEmail, subject, body!!)
            }
        } else {
            // Handle the case when sender or messageBody is null (e.g., show an error message)
        }
    }

    private fun addSmsToRecyclerView(smsStrings: SmsStrings) {
        smsAdapter?.addSms(SmsData(smsStrings))
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


    private fun reassembleLongCheckedSms(context: Context, recipient: String, pdus: Array<Any>): SmsStrings {
        var completeMessage = ""
        var sender: String = ""
        var timeInSeconds: Long = 0


        for (pdu in pdus){
            val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
            val userData = smsMessage.userData
            val messageBody = smsMessage.messageBody

            sender = smsMessage.displayOriginatingAddress

            if(userData != null && userData.size > 1){
                completeMessage += messageBody
            }
        }

        val smsStrings = SmsStrings(sender, recipient, completeMessage, Calendar.getInstance())


        return smsStrings
    }



}

