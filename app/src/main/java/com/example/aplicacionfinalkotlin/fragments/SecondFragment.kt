package com.example.aplicacionfinalkotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.models.Sword
import com.example.aplicacionfinalkotlin.ui.main.PlaceholderFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class SecondFragment : PlaceholderFragment() {
    private var dbReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var user: com.example.aplicacionfinalkotlin.models.User
    var sword:Sword?= null
    var key= false
    var ring=0
    var healthPotion=0
    var staminaPotion=0
    lateinit var ivSword: ImageView
    lateinit var ivKey: ImageView
    lateinit var ivring1: ImageView
    lateinit var ivring2: ImageView
    lateinit var ivhp1: ImageView
    lateinit var ivhp2: ImageView
    lateinit var ivsp1: ImageView
    lateinit var ivsp2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivSword = requireView().findViewById<View>(R.id.ivSword) as ImageView
        ivKey = requireView().findViewById<View>(R.id.ivKey) as ImageView
        ivring1 = requireView().findViewById<View>(R.id.ivring1) as ImageView
        ivring2 = requireView().findViewById<View>(R.id.ivring2) as ImageView
        ivhp1 = requireView().findViewById<View>(R.id.ivhp1) as ImageView
        ivhp2 = requireView().findViewById<View>(R.id.ivhp2) as ImageView
        ivring2 = requireView().findViewById<View>(R.id.ivring2) as ImageView
        ivsp1 = requireView().findViewById<View>(R.id.ivsp1) as ImageView
        ivsp2 = requireView().findViewById<View>(R.id.ivsp2) as ImageView


        //User uid extracted
        mAuth = FirebaseAuth.getInstance()
        var uid= mAuth!!.uid

        //Database initialization
        database = FirebaseDatabase.getInstance()
        dbReference = database!!.reference.child("User")
        dbReference = FirebaseDatabase.getInstance().reference.child("User").child(uid.toString())
        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user= dataSnapshot.getValue(com.example.aplicacionfinalkotlin.models.User::class.java)!!

                sword= user.sword
                ring=user.ring
                healthPotion=user.healthPotion
                staminaPotion=user.staminaPotion
                key=user.key

                if(sword!=null) ivSword.visibility=View.VISIBLE
                if(key) ivKey.visibility=View.VISIBLE
                if(healthPotion==1) ivhp1.visibility=View.VISIBLE
                if(healthPotion>1) ivhp2.visibility=View.VISIBLE
                if(staminaPotion==1) ivsp1.visibility=View.VISIBLE
                if(staminaPotion>1) ivsp2.visibility=View.VISIBLE
                if(ring==1) ivring1.visibility=View.VISIBLE
                if(ring>1) ivring1.visibility=View.VISIBLE

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
        dbReference!!.addValueEventListener(eventListener)



    }

}