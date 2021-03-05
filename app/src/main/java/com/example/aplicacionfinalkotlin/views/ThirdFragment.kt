package com.example.aplicacionfinalkotlin.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.controllers.main.PlaceholderFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ThirdFragment : PlaceholderFragment() {
    lateinit var btnQuest1: Button
    lateinit var btnQuest2: Button
    lateinit var btnQuest3: Button
    lateinit var btnQuest4: Button
    lateinit var btnQuest5: Button
    lateinit var btnQuest6: Button

    private var dbReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var user: com.example.aplicacionfinalkotlin.models.User

    private var userDmg=1
    private var userHealthPotions=0

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

        btnQuest1 = requireView().findViewById<View>(R.id.btnQuest1) as Button
        btnQuest2 = requireView().findViewById<View>(R.id.btnQuest2) as Button
        btnQuest3 = requireView().findViewById<View>(R.id.btnQuest3) as Button
        btnQuest4 = requireView().findViewById<View>(R.id.btnQuest4) as Button
        btnQuest5 = requireView().findViewById<View>(R.id.btnQuest5) as Button
        btnQuest6 = requireView().findViewById<View>(R.id.btnQuest6) as Button


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
                userDmg= user.sword!!.dmg
                userHealthPotions=user.healthPotion
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
        dbReference!!.addValueEventListener(eventListener)

        //General intent extras
        val intent = Intent(context, QuestActivity::class.java)
        intent.putExtra("dmg", userDmg)
        intent.putExtra("potions", userHealthPotions)

        /*
        * On Click for each level
        * */
        val quest1 = View.OnClickListener {view ->
            //Indication of level
            intent.putExtra("level",1)
            //The new activity is launched
            this.startActivity(intent)
            }
        btnQuest1.setOnClickListener(quest1)

        val quest2 = View.OnClickListener {view ->
            //Indication of level
            intent.putExtra("level",2)
            //The new activity is launched
            this.startActivity(intent)
        }
        btnQuest2.setOnClickListener(quest2)

        val quest3 = View.OnClickListener {view ->
            //Indication of level
            intent.putExtra("level",3)
            //The new activity is launched
            this.startActivity(intent)
        }
        btnQuest3.setOnClickListener(quest3)

        val quest4 = View.OnClickListener {view ->
            //Indication of level
            intent.putExtra("level",4)
            //The new activity is launched
            this.startActivity(intent)
        }
        btnQuest4.setOnClickListener(quest4)

        val quest5 = View.OnClickListener {view ->
            //Indication of level
            intent.putExtra("level",5)
            //The new activity is launched
            this.startActivity(intent)
        }
        btnQuest5.setOnClickListener(quest5)

        val quest6 = View.OnClickListener {view ->
            //Indication of level
            intent.putExtra("level",6)
            //The new activity is launched
            this.startActivity(intent)
        }
        btnQuest6.setOnClickListener(quest6)
    }
}
