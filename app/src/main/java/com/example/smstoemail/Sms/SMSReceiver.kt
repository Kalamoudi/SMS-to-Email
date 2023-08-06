package com.example.smstoemail.Sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import com.example.smstoemail.Email.HandleEmail
import com.example.smstoemail.Utils
import com.example.smstoemail.userEmail
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


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
        var smsMap: Map<String, String>

        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            // Handle SMS messages
            val bundle = intent.extras

            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<Any>?
                if (pdus != null) {
                    // Check if the message is part of a long SMS
                    smsMap = reassembleLongCheckedSms(context, recipient, pdus)
                    onSmsReceived(context, smsMap)
                }
            }
        } else if (intent.action == "android.provider.Telephony.RCS_MESSAGE_RECEIVED") {
            // Handle RCS messages
            // Retrieve the RCS message data (sender and message body) from the intent extras
            val sender = intent.getStringExtra("sender")
            val messageBody = intent.getStringExtra("message_body")


            // Notify the listener that an RCS message has been received
            smsMap = dataToMap(sender!!, recipient, messageBody!!)
            onSmsReceived(context, smsMap)
        }
    }


    fun onSmsReceived(context: Context, smsMap: Map<String, String>) {
        if (smsMap["sender"] != null && smsMap["messageBody"] != null) {
            // Add the received SMS or RCS message to the RecyclerView
            if(Utils.isValidEmail(userEmail)) {
                addSmsToRecyclerView(smsMap)
            }

            // Send email with the message content
            val emailSender = "khalid.smssender@mailsac.com" // Replace with your email address
            val recipient = "khalamoudi91@gmail.com"
            val subject = "SMS from $smsMap[\"sender\"] to $recipient"
            val body = smsMap["messageBody"]

            // Call the sendEmail function with all required arguments
            if(Utils.isValidEmail(userEmail)) {
                handleEmail = HandleEmail()
                handleEmail.sendEmail(context, userEmail, subject, body!!)
            }
        } else {
            // Handle the case when sender or messageBody is null (e.g., show an error message)
        }
    }

    private fun addSmsToRecyclerView(smsMap: Map<String, String>) {
        smsAdapter?.addSms(SmsData(smsMap))
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


    private fun reassembleLongCheckedSms(context: Context, recipient: String, pdus: Array<Any>): Map<String, String>{
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

        val smsMap = dataToMap(sender, recipient, completeMessage)


        return smsMap
    }


    private fun dataToMap(sender: String, recipient: String, completeMessage: String):
            Map<String, String> {


        val calendar = Calendar.getInstance()

        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> "Unknown"
        }

        val smsMap = mapOf(
            "sender" to sender,
            "recipient" to recipient,
            "messageBody" to completeMessage,
            "dayOfWeek" to dayOfWeek,
            "day" to (calendar.get(Calendar.DAY_OF_MONTH)).toString(),
            "month" to (calendar.get(Calendar.MONTH) + 1).toString(),
            "year" to calendar.get(Calendar.YEAR).toString(),
            "seconds" to calendar.get(Calendar.SECOND).toString(),
            "minutes" to calendar.get(Calendar.MINUTE).toString(),
            "hour" to calendar.get(Calendar.HOUR).toString(),
            "meridiem" to calendar.get(Calendar.AM_PM).toString()
        )

        return smsMap
    }

}

