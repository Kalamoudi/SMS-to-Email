package com.example.smstoemail.Sms

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.Interfaces.RecyclerMessageDao
import com.example.smstoemail.Interfaces.recyclerMessageDao
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SMSAdapter : RecyclerView.Adapter<SMSAdapter.SMSViewHolder>() {

    private val smsList = mutableListOf<SmsData>()

    fun addSms(smsData: SmsData) {
        smsList.add(smsData)
        notifyItemInserted(smsList.size - 1)
        val smsDataInRecyclerMessage = RecyclerMessage(0, smsData.sender, smsData.recipient, smsData.message)

        // saveNewItem needs to be called from a suspend function or courotine since it's Async task
        CoroutineScope(Dispatchers.IO).launch {
            //Utils.saveNewItem(smsDataInRecyclerMessage)
            Utils.saveNewItem(smsDataInRecyclerMessage, recyclerMessageDao)
        }
        Log.d("SMSAdapter", "SMS added to the list")
    }

    // Function to update/load the messages that were already saved on the AppDatabase (tablename=RecyclerMessage)
    fun updateSmsList(RecyclerMessages: List<RecyclerMessage>) {
        val fetchedSmsList = mutableListOf<SmsData>()

        for(RecyclerMessage in RecyclerMessages){
            val smsData = SmsData(RecyclerMessage.sender, RecyclerMessage.recipient, RecyclerMessage.messageBody)
            fetchedSmsList.add(smsData)
        }
        smsList.clear()
        smsList.addAll(fetchedSmsList)
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
         //   itemView.findViewById<TextView>(R.id.senderTextView).text = smsData.sender
         //   itemView.findViewById<TextView>(R.id.recipientTextView).text = smsData.receiver
            itemView.findViewById<TextView>(R.id.messageHeader).text = "From " + smsData.sender + " to " + smsData.recipient
            itemView.findViewById<TextView>(R.id.messageTextView).text = smsData.message
        }
    }
}
