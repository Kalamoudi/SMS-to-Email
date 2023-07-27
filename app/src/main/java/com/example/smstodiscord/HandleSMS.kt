package com.example.smstodiscord

import android.content.Context
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView




class HandleSMS {

    private lateinit var smsRecyclerView: RecyclerView
    private lateinit var smsAdapter: SMSAdapter
    private lateinit var smsReceiver: SMSReceiver

    fun handleSMS(context: Context) {
        smsAdapter = SMSAdapter()
        smsReceiver = SMSReceiver()
        SMSReceiver.setSMSAdapter(smsAdapter)


        smsRecyclerView = (context as AppCompatActivity).findViewById(R.id.smsRecyclerView)
        smsRecyclerView.adapter = smsAdapter
        smsRecyclerView.layoutManager = LinearLayoutManager(context)

        //val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        //context.registerReceiver(smsReceiver, filter)
    }


}