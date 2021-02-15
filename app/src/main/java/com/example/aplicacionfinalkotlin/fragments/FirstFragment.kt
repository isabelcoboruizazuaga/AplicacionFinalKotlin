package com.example.aplicacionfinalkotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.ui.main.PlaceholderFragment

class FirstFragment : PlaceholderFragment() {
    var stateText1: TextView? = null
    var btnOption1: Button? = null
    var btnOption2: Button? = null
    var btnOption3: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateText1 = getView()!!.findViewById<View>(R.id.txtState1) as TextView
        btnOption1 = getView()!!.findViewById<View>(R.id.btnOption1) as Button
        btnOption2 = getView()!!.findViewById<View>(R.id.btnOption2) as Button
        btnOption3 = getView()!!.findViewById<View>(R.id.btnOption3) as Button
        btnOption1!!.setOnClickListener { }
        changeStateText()
    }

    fun changeStateText() {
        stateText1!!.text = "La noche se acerca..."
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        btnOption1!!.visibility = View.VISIBLE
        btnOption2!!.visibility = View.VISIBLE
        btnOption1!!.text = "Huir"
        btnOption2!!.text = "Buscar Refugio"
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}