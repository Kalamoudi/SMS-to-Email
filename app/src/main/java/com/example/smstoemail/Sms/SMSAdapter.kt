package com.example.smstoemail.Sms

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.R

class SMSAdapter : RecyclerView.Adapter<SMSAdapter.SMSViewHolder>() {

    private val smsList = mutableListOf<SmsData>()

    fun addSms(smsData: SmsData) {
        smsList.add(smsData)
        notifyItemInserted(smsList.size - 1)
        Log.d("SMSAdapter", "SMS added to the list")
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
            itemView.findViewById<TextView>(R.id.senderTextView).text = smsData.sender
            itemView.findViewById<TextView>(R.id.receiverTextView).text = smsData.receiver
            itemView.findViewById<TextView>(R.id.messageTextView).text = smsData.message
        }
    }
}
