package com.example.smstoemail.Sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.example.smstoemail.Email.HandleEmail
import com.example.smstoemail.userEmail


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
                        onSmsReceived(context, sender, messageBody)
                    }
                }
            }
        } else if (intent.action == "android.provider.Telephony.RCS_MESSAGE_RECEIVED") {
            // Handle RCS messages
            // Retrieve the RCS message data (sender and message body) from the intent extras
            val sender = intent.getStringExtra("sender")
            val messageBody = intent.getStringExtra("message_body")

            // Notify the listener that an RCS message has been received
            onSmsReceived(context, sender, messageBody)
        }
    }

    fun onSmsReceived(context: Context, sender: String?, messageBody: String?) {
        if (sender != null && messageBody != null) {
            // Add the received SMS or RCS message to the RecyclerView
            if(isValidEmail(userEmail)) {
                addSmsToRecyclerView(sender, messageBody)
            }

            // Send email with the message content
            val emailSender = "khalid.smssender@mailsac.com" // Replace with your email address
            val recipient = "khalamoudi91@gmail.com"
            val subject = "SMS from $sender"
            val body = "$messageBody"

            // Call the sendEmail function with all required arguments
            if(isValidEmail(userEmail)) {
                handleEmail = HandleEmail()
                handleEmail.sendEmail(context, userEmail, subject, body)
            }
        } else {
            // Handle the case when sender or messageBody is null (e.g., show an error message)
        }
    }

    private fun addSmsToRecyclerView(sender: String, message: String) {
        smsAdapter?.addSms(SmsData(sender, message))
    }

    fun isValidEmail(email: String): Boolean {
        // Regular expression pattern for a valid email address
        val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

        return emailPattern.matches(email)
    }

}

