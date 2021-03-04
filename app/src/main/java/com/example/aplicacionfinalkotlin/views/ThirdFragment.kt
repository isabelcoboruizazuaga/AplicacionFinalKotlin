package com.example.aplicacionfinalkotlin.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.controllers.main.PlaceholderFragment

class ThirdFragment : PlaceholderFragment() {
    lateinit var btnQuest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnQuest = requireView().findViewById<View>(R.id.btnQuest) as Button

        val quest1 = View.OnClickListener {view ->
            startActivity(Intent(context,QuestActivity::class.java))
            }
        btnQuest.setOnClickListener(quest1)
    }
}
