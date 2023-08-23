package com.example.smstoemail.SmsFilters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.Entity.SmsFilterRecyclerMessage
import com.example.smstoemail.Interfaces.recyclerMessageDao
import com.example.smstoemail.Interfaces.smsFilterRecyclerMessageDao
import com.example.smstoemail.R
import com.example.smstoemail.Sms.SmsData
import com.example.smstoemail.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedList

class SmsFiltersAdapter() : RecyclerView.Adapter<SmsFiltersAdapter.SmsFilterViewHolder>() {


    private val smsFilterList = mutableListOf<SmsFilter>()

    fun addSmsFilter(smsFilter: SmsFilter) {
        if(smsFilterList.contains(SmsFilter(smsFilter.filter.lowercase()))){
            return
        }
        smsFilterList.add(SmsFilter(smsFilter.filter.lowercase()))

        notifyItemInserted(smsFilterList.size - 1)

        val smsFilterInRecyclerMessage = SmsFilterRecyclerMessage(
            0,
            smsFilter.filter.lowercase(),
        )


        // saveNewItem needs to be called from a suspend function or courotine since it's Async task
        CoroutineScope(Dispatchers.IO).launch {
            //Utils.saveNewItem(smsDataInRecyclerMessage)
            Utils.saveNewItem(smsFilterInRecyclerMessage, smsFilterRecyclerMessageDao)
        }
        //Log.d("SMSAdapter", "SMS added to the list")
        notifyDataSetChanged()
        //notifyItemChanged(0)
    }



    // Function to update/load the messages that were already saved on the AppDatabase (tablename=RecyclerMessage)
    fun updateSmsFilterList(smsFilterRecyclerMessages: List<SmsFilterRecyclerMessage>) {
        val fetchedSmsList = mutableListOf<SmsFilter>()

        for(smsFilterRecyclerMessage in smsFilterRecyclerMessages){
                val smsFilter = SmsFilter(smsFilterRecyclerMessage.filter)

            fetchedSmsList.add(smsFilter)
        }
        smsFilterList.clear()

        for(smsFilter in fetchedSmsList){
            smsFilterList.add(smsFilter)
        }
        notifyDataSetChanged()
    }

    fun removeSmsFilter(smsFilter: SmsFilter) {
      //  val removeElement = smsFilterList.indexOf(smsFilter)
      //  smsFilterList[removeElement] = smsFilterList[smsFilterList.size -1]
      //  smsFilterList.removeLast()
        val index = smsFilterList.indexOf(smsFilter)
        smsFilterList.removeAt(index)


        notifyItemRemoved(index)



        // saveNewItem needs to be called from a suspend function or coroutine since it's Async task
        CoroutineScope(Dispatchers.IO).launch {
            //Utils.saveNewItem(smsDataInRecyclerMessage)
            smsFilterRecyclerMessageDao.delete(smsFilterRecyclerMessageDao.getSmsFilter(smsFilter.filter))
            //  Utils.saveNewItem(smsFilterInRecyclerMessage, smsFilterRecyclerMessageDao)
        }
        //Log.d("SMSAdapter", "SMS added to the list")
        notifyDataSetChanged()
        //notifyItemChanged(0)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsFilterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sms_filter_item, parent, false)
        return SmsFilterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SmsFilterViewHolder, position: Int) {
        val smsFilter = smsFilterList[position]
        holder.bind(smsFilter)
    }

    override fun getItemCount(): Int {

        return smsFilterList.size
    }

    fun getSmsFilterList(): List<SmsFilter> {
        return smsFilterList.toList()
    }

    inner class SmsFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind sms filters to the item view

//        fun bind(smsFilter: SmsFilter) {
//
//            itemView.findViewById<TextView>(R.id.smsFilter).text = smsFilter.filter
//        }
        fun bind(smsFilter: SmsFilter) {
            val smsFilterLayout = itemView.findViewById<RelativeLayout>(R.id.smsFilterLayout)
            val smsFilterTextView = itemView.findViewById<TextView>(R.id.smsFilter)
            val smsFilterDeleteButton = itemView.findViewById<ImageView>(R.id.smsFilterDeleteButton)

            smsFilterTextView.text = smsFilter.filter

            // Measure the width of the text view
            smsFilterTextView.measure(smsFilterTextView.width, 0)
            val textWidth = smsFilterTextView.measuredWidth
            val textHeight = smsFilterTextView.measuredHeight

            smsFilterDeleteButton.measure(smsFilterDeleteButton.width, smsFilterDeleteButton.height)
            val deleteButtonWidth = smsFilterDeleteButton.measuredWidth
            val deleteButtonHeight = smsFilterDeleteButton.measuredHeight

            // Set the width of the RelativeLayout to match the width of the text
            val layoutParams = smsFilterLayout.layoutParams
            layoutParams.width = textWidth + deleteButtonWidth
            layoutParams.height = deleteButtonHeight
            smsFilterLayout.layoutParams = layoutParams


            // Remove filter from recycler view
            smsFilterDeleteButton.setOnClickListener {
                removeSmsFilter(SmsFilter(smsFilterTextView.text.toString()))

            }
        }

    }
}