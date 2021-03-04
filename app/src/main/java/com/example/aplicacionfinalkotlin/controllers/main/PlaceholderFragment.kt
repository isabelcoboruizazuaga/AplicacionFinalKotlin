package com.example.aplicacionfinalkotlin.controllers.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.views.FirstFragment
import com.example.aplicacionfinalkotlin.views.SecondFragment
import com.example.aplicacionfinalkotlin.views.ThirdFragment

/**
 * A placeholder fragment containing a simple view.
 */
open class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)
        var index = 1
        if (arguments != null) {
            index = requireArguments().getInt(ARG_SECTION_NUMBER)
        }
        pageViewModel.setIndex(index)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        pageViewModel.text.observe(viewLifecycleOwner, Observer<String> {
            textView.text = it
        })
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment?? {
            var placeholderFragment: PlaceholderFragment??= null
            when (sectionNumber) {
                1 -> placeholderFragment = FirstFragment()
                2 -> placeholderFragment = SecondFragment()
                3 -> placeholderFragment = ThirdFragment()
            }
            return placeholderFragment
        }
    }
}