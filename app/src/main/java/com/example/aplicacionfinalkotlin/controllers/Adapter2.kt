package com.example.aplicacionfinalkotlin.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.models.Message
import com.example.aplicacionfinalkotlin.models.User

class Adapter2
//Adapter2's constructor
    (private val messageList: List<Message>) : RecyclerView.Adapter<Adapter2.Adapter2ViewHolder>() {
    private var context: Context? = null
    private val user: User? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter2ViewHolder {
        //The view is inflated
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false)

        //The view holder is created
        return Adapter2ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Adapter2ViewHolder, position: Int) {
        val messageItem = messageList[position]

        //The messages are put into the layout
        holder.txt_message.text = messageItem.messageBody
        holder.txt_name.text = messageItem.transmitterName
    }

    override fun getItemCount(): Int {
        return try {
            messageList.size
        } catch (e: Exception) {
            0
        }
    }

    inner class Adapter2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Layout items
        val txt_message: TextView
        val txt_name: TextView

        //ViewHolder constructor
        init {

            //The context is given the correct value
            context = itemView.context

            //Layout items initialization
            txt_message = itemView.findViewById<View>(R.id.txtMessage) as TextView
            txt_name = itemView.findViewById<View>(R.id.txt_userName) as TextView
        }
    }

}