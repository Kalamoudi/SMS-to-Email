package com.kuaapps.smstoemail.NavigationDrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.kuaapps.smstoemail.R

class NavDrawerMainAdapter (context: Context, private val data: List<String>, private val imageList: List<Int>) :
    ArrayAdapter<String>(context, R.layout.nav_drawer_main_list_item, R.id.mainListItemTextView, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.nav_drawer_main_list_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.mainListItemTextView)
        val imageView = view.findViewById<ImageView>(R.id.mainListItemIconImageView)

        textView.text = data[position]
        imageView.setImageResource(imageList[position])

        return view
    }
}