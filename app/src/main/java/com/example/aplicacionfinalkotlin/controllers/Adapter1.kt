package com.example.aplicacionfinalkotlin.controllers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.views.ChatActivity
import com.example.aplicacionfinalkotlin.models.User
import java.util.ArrayList


class Adapter1 //Adapter1's constructor
    (private val userList: ArrayList<User>) : RecyclerView.Adapter<Adapter1.Adapter1ViewHolder>() {
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter1ViewHolder {
        //The view is inflated
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)

        //The view holder is created
        return Adapter1ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Adapter1ViewHolder, position: Int) {
        //The user data are put into the layout
        val userItem = userList[position]
        holder.txt_nombre.text = userItem.name
        holder.txt_email.text = userItem.email
        holder.iv_photo.setImageResource(R.mipmap.ic_launcher)

        //Each item will have an OnClickListener
        holder.itemView.setOnClickListener {
            //When an item is pressed the chat activity will be launched
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("uidContact", userItem.uid)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class Adapter1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Layout items
        val txt_nombre: TextView
        val txt_email: TextView
        val iv_photo: ImageView

        //ViewHolder constructor
        init {

            //The context is given the correct value
            context = itemView.context

            //Layout items initialization
            txt_nombre = itemView.findViewById<View>(R.id.txtUserName) as TextView
            txt_email = itemView.findViewById<View>(R.id.txtUserEmail) as TextView
            iv_photo = itemView.findViewById<View>(R.id.imageView) as ImageView
        }
    }

}