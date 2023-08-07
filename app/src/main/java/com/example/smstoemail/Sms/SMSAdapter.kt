package com.example.smstoemail.Sms

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.Interfaces.recyclerMessageDao
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedList

class SMSAdapter(showAmount: Int = 100) : RecyclerView.Adapter<SMSAdapter.SMSViewHolder>() {

  //  private val smsList = mutableListOf<SmsData>()
    private val smsList = LinkedList<SmsData>()
    private var showAmount: Int = showAmount


    fun addSms(smsData: SmsData) {
        smsList.addFirst(smsData)

        notifyItemInserted(0)
//        val smsDataInRecyclerMessage = RecyclerMessage(0, smsData.smsStrings.sender, smsData.smsStrings.recipient,
//            smsData.smsStrings.messageBody, smsData.smsStrings.dayOfWeek, smsData.smsStrings.day, smsData.smsStrings.month,
//            smsData.smsStrings.year, smsData.smsStrings.seconds, smsData.smsStrings.minutes, smsData.smsStrings.hour,
//            smsData.smsStrings.meridiem)

        val smsDataInRecyclerMessage = RecyclerMessage(0, smsData.smsStrings.sender, smsData.smsStrings.recipient,
            smsData.smsStrings.messageBody, smsData.smsStrings.calendar)

        // saveNewItem needs to be called from a suspend function or courotine since it's Async task
        CoroutineScope(Dispatchers.IO).launch {
            //Utils.saveNewItem(smsDataInRecyclerMessage)
            Utils.saveNewItem(smsDataInRecyclerMessage, recyclerMessageDao)
        }
        //Log.d("SMSAdapter", "SMS added to the list")
        notifyDataSetChanged()
    }

    // Function to update/load the messages that were already saved on the AppDatabase (tablename=RecyclerMessage)
    fun updateSmsList(RecyclerMessages: List<RecyclerMessage>) {
        val fetchedSmsList = mutableListOf<SmsData>()

        for(RecyclerMessage in RecyclerMessages){
            Log.d("dataFound", "Found some data yo")
           // val smsData = SmsData(RecyclerMessage.sender, RecyclerMessage.recipient, RecyclerMessage.messageBody)
            val smsData = SmsData(SmsStrings(RecyclerMessage.sender, RecyclerMessage.recipient,
                                            RecyclerMessage.messageBody, RecyclerMessage.calendar)
            )
            fetchedSmsList.add(smsData)
        }
        smsList.clear()
        if(showAmount > fetchedSmsList.size){
            showAmount = fetchedSmsList.size-1
        }
        var i = 0
        while(i < showAmount){
            smsList.add(fetchedSmsList[fetchedSmsList.size-1-i])
            i += 1
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sms, parent, false)
        return SMSViewHolder(view)
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        val smsData = smsList[position]
        holder.bind(smsData)
    }

    override fun getItemCount(): Int {
        return smsList.size
    }

    class SMSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind SMS data to the item view

        fun bind(smsData: SmsData) {
           // val timeExtension = if (smsData.smsMap["hour"]?.toInt() ?: 0 > 12) "PM" else "AM"
            itemView.findViewById<TextView>(R.id.messageDateAndTime).text = smsData.smsStrings.binder().header
            itemView.findViewById<TextView>(R.id.senderTextView).text = smsData.smsStrings.binder().sender
            itemView.findViewById<TextView>(R.id.recipientTextView).text = smsData.smsStrings.binder().recipient
          //  itemView.findViewById<TextView>(R.id.messageHeader).text = "From " + smsData.smsMap["sender"] + " to " + smsData.smsMap["recipient"]
            itemView.findViewById<TextView>(R.id.messageTextView).text = smsData.smsStrings.binder().messageBody
        }

    }
}
