package com.example.smstoemail.NavigationDrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.smstoemail.R

class NavDrawerSecondAdapter (context: Context, private val data: List<String>) :
    ArrayAdapter<String>(context, R.layout.nav_drawer_sec_list_item, R.id.secondaryListItemTextView, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.nav_drawer_sec_list_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.secondaryListItemTextView)

        textView.text = data[position]

        return view
    }
}