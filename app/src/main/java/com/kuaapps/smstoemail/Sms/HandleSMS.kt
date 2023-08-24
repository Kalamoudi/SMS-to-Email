package com.kuaapps.smstoemail.Sms

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuaapps.smstoemail.Interfaces.recyclerMessageDao
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.Repository.AppDatabase
import com.kuaapps.smstoemail.smsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HandleSMS() {


    private lateinit var smsRecyclerView: RecyclerView
    private lateinit var smsReceiver: SMSReceiver

    fun handleSMS(context: Context) {
        smsAdapter = SMSAdapter()
        smsReceiver = SMSReceiver()
        SMSReceiver.setSMSAdapter(smsAdapter)

        // Gather recycler messages from database and calls smsAdapter's update method
        processRecyclerMessagesDao(context)
//
//        val smsDataList = recyclerMessages.map { SmsData(it.sender, it.recipient, it.messageBody) }
//        smsAdapter.updateSmsList(smsDataList)


        smsRecyclerView = (context as AppCompatActivity).findViewById(R.id.smsRecyclerView)
        smsRecyclerView.adapter = smsAdapter
        smsRecyclerView.layoutManager = LinearLayoutManager(context)

        //val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        //context.registerReceiver(smsReceiver, filter)
    }


    private fun processRecyclerMessagesDao(context: Context){
        GlobalScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getInstance(context)
            recyclerMessageDao = database.recyclerMessageDao()
            val recyclerMessages = recyclerMessageDao.getAllItems()
            smsAdapter.updateSmsList(recyclerMessages)
            // Use the 'items' in the UI if needed (e.g., update the UI with the data)
        }
    }

}