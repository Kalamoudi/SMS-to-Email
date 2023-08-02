package com.example.smstoemail.Sms

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.R
import com.example.smstoemail.Repository.AppDatabase
import com.example.smstoemail.itemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HandleSMS {


    private lateinit var smsRecyclerView: RecyclerView
    private lateinit var smsAdapter: SMSAdapter
    private lateinit var smsReceiver: SMSReceiver

    fun handleSMS(context: Context) {
        smsAdapter = SMSAdapter()
        smsReceiver = SMSReceiver()
        SMSReceiver.setSMSAdapter(smsAdapter)


        GlobalScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getInstance(context)
            itemDao = database.itemDao()
            val recyclerMessages = itemDao.getAllItems()
            smsAdapter.updateSmsList(recyclerMessages)
            // Use the 'items' in the UI if needed (e.g., update the UI with the data)
        }
//
//        val smsDataList = recyclerMessages.map { SmsData(it.sender, it.recipient, it.messageBody) }
//        smsAdapter.updateSmsList(smsDataList)


        smsRecyclerView = (context as AppCompatActivity).findViewById(R.id.smsRecyclerView)
        smsRecyclerView.adapter = smsAdapter
        smsRecyclerView.layoutManager = LinearLayoutManager(context)

        //val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        //context.registerReceiver(smsReceiver, filter)
    }


}