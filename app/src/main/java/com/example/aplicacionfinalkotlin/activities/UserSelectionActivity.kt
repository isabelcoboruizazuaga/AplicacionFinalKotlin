package com.example.aplicacionfinalkotlin.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.controllers.Adapter1
import com.example.aplicacionfinalkotlin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class UserSelectionActivity : AppCompatActivity() {
    private var recView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var dbReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private val uid: String? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var user: User
    private lateinit var userList: ArrayList<User>
    override fun onBackPressed() {
        mAuth = FirebaseAuth.getInstance()
        mAuth!!.signOut()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_selection)

        //Database initialization
        database = FirebaseDatabase.getInstance()
        dbReference = database!!.reference.child("User")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList = ArrayList()

                //Loop to retrieve user data
                for (xuser in dataSnapshot.children) {
                    //User list creation
                    user = xuser.getValue(User::class.java)!!
                    userList!!.add(user)
                    Log.e("onDataChange", "onDataChange:" + xuser.value.toString())
                }

                //RecyclerView initialization
                recView = findViewById<View>(R.id.recyclerView) as RecyclerView

                //Assignment of the Layout to the Recycler View
                layoutManager = LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false)
                recView!!.layoutManager = layoutManager

                //Assignment of the Recycler View adapter with the user list
                adapter = Adapter1(userList)
                recView!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
        //The event listened is added to the database
        dbReference!!.addValueEventListener(eventListener)
    }
}