package com.example.aplicacionfinalkotlin.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.controllers.Adapter2
import com.example.aplicacionfinalkotlin.models.Message
import com.example.aplicacionfinalkotlin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ChatActivity : AppCompatActivity() {
    //Objects and Lists
    private lateinit var messageList: MutableList<Message>
    private lateinit var receivedList:MutableList<Message>
    private lateinit var sentList: MutableList<Message>
    private lateinit var mySentList: MutableList<Message>
    private lateinit var myreceivedList: MutableList<Message>
    private lateinit var currentUserObject: User
    private lateinit var currentUser: User
    private lateinit var contactUserObject: User
    lateinit var messageObject: Message

    //Recycler View variables
    private var recView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<*>? = null

    //Firebase database variables//
    private var database: FirebaseDatabase? = null

    //General variables (Actualize Recycler View)
    private var dbReferenceCurrentUser: DatabaseReference? = null
    private var eventListener: ValueEventListener? = null

    //Specific variables to send messages
    private var dbReferenceSendMessageCurrentUser: DatabaseReference? = null
    private var eventListenerSendMessageCurrentUserPart: ValueEventListener? = null
    private var dbReferenceSendMessageContactUser: DatabaseReference? = null
    private var eventListenerSendMessageContactUserPart: ValueEventListener? = null

    //Other firebase variables
    private var mAuth: FirebaseAuth? = null
    private var actualUid: String? = null
    private var contactUid: String? = null

    //Layout variables
    private var linearLayout: LinearLayout? = null
    private var btnSend: ImageButton? = null
    private var txtSendMessage: TextView? = null
    private var messageBody: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_selection)

        //Uids initialization
        var myintent = getIntent()
        contactUid = myintent.getStringExtra("uidContact")
        mAuth = FirebaseAuth.getInstance()
        actualUid = mAuth!!.currentUser!!.uid

        //Lists initialization
        messageList = ArrayList()
        receivedList = ArrayList()
        sentList = ArrayList()

        //Database initiaization
        database = FirebaseDatabase.getInstance()
        dbReferenceCurrentUser = database!!.reference.child("User").child(actualUid!!)
        dbReferenceSendMessageCurrentUser = dbReferenceCurrentUser
        dbReferenceSendMessageContactUser = database!!.reference.child("User").child(contactUid!!)

        //The event listeners are added to the database
        //General
        setEventListener()
        dbReferenceCurrentUser!!.addValueEventListener(eventListener!!)
        //Specifics
        setEventListenerSendMessageCurrentUserPart()
        dbReferenceSendMessageCurrentUser!!.addValueEventListener(eventListenerSendMessageCurrentUserPart!!)
        setEventListenerSendMessageContactUserPart()
        dbReferenceSendMessageContactUser!!.addValueEventListener(eventListenerSendMessageContactUserPart!!)


        //Layout settings
        linearLayout = findViewById(R.id.myLinearLayout)
        linearLayout!!.setVisibility(View.VISIBLE)
        txtSendMessage = findViewById(R.id.txtSendMessage)
        btnSend = findViewById(R.id.btnSend)
        btnSend!!.setOnClickListener(View.OnClickListener { sendMessage() })
    }

    //Database listener
    fun setEventListener() {
        eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //The data of the logged user are extracted
                currentUserObject = dataSnapshot.getValue(User::class.java)!!

                //The list of the received messages is gotten
                currentUserReceivedMessages

                //Same with the sent messages
                currentUserSentMessages
                RV_creation()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
    }//When a message is from the contact it is added to the received List//The Received list is reseted

    //The received messages are extracted

    //The program goes through the list

    //Method to get the current user received messages
    val currentUserReceivedMessages: Unit
        get() {
            //The Received list is reseted
            receivedList!!.clear()

            //The received messages are extracted
            val auxiliarMessageList = currentUserObject!!.receivedList

            //The program goes through the list
            for (i in auxiliarMessageList.indices) {
                val messageObject = auxiliarMessageList[i]
                //When a message is from the contact it is added to the received List
                if (messageObject.transmitter == contactUid) {
                    receivedList!!.add(messageObject)
                }
            }
        }//When a message receiver is contact it is added to the received list//The Sent list is reseted

    //The sent messages are extracted

    //The program goes through the list

    //Method to get the current user sent messages
    val currentUserSentMessages: Unit
        get() {
            //The Sent list is reseted
            sentList!!.clear()

            //The sent messages are extracted
            val auxiliarMessageList = currentUserObject!!.sentList

            //The program goes through the list
            for (i in auxiliarMessageList.indices) {
                val messageObject = auxiliarMessageList[i]
                //When a message receiver is contact it is added to the received list
                if (messageObject.receiver == contactUid) {
                    sentList!!.add(messageObject)
                }
            }
        }

    //Method to send messages
    fun sendMessage() {
        //We get the message body and empty the message field
        messageBody = txtSendMessage!!.text.toString()
        txtSendMessage!!.text = ""

        //The message is created, the sender is the current user and the contact is the receiver
        messageObject = Message(actualUid, contactUid, messageBody, currentUser!!.name)
        /*
         * Changes in current user. To obtain the data the program uses the method setEventListenerSendMessageCurrentUserPart()
         */
        //The message is added to the list obtained in that method
        mySentList!!.add(messageObject!!)

        //The list is added to the user and it is saved inside the database
        currentUser!!.sentList = mySentList!!
        dbReferenceSendMessageCurrentUser!!.setValue(currentUser)

        /*
         * Changes in contact user. To obtain the data the program uses the method setEventListenerSendMessageContactUserPart()
         */
        //The message is added to the list obtained in that method
        myreceivedList!!.add(messageObject!!)

        //The list is added to the user and it is saved inside the database
        contactUserObject!!.receivedList = myreceivedList!!
        dbReferenceSendMessageContactUser!!.setValue(contactUserObject)
    }

    //Method to actualize current user sent list
    fun setEventListenerSendMessageCurrentUserPart() {
        eventListenerSendMessageCurrentUserPart = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //The data of the logged user are extracted
                currentUser = dataSnapshot.getValue(User::class.java)!!

                //The sent list is obtained
                mySentList = currentUser!!.sentList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
    }

    //Method to actualize contact user received list
    fun setEventListenerSendMessageContactUserPart() {
        eventListenerSendMessageContactUserPart = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //The data of the contact user are extracted
                contactUserObject = dataSnapshot.getValue(User::class.java)!!

                //The received list is obtained
                myreceivedList = contactUserObject!!.receivedList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
    }

    //Creation of the Recycler View
    fun RV_creation() {
        //RecyclerView initialization
        recView = findViewById<View>(R.id.recyclerView) as RecyclerView

        //Assignment of the Layout to the Recycler View
        layoutManager = LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false)
        recView!!.layoutManager = layoutManager

        //Assignment of the Recycler View adapter with the user list, wich is a merged List of the other two
        messageList!!.clear()
        messageList!!.addAll(receivedList!!)
        messageList!!.addAll(sentList!!)
        Collections.sort(messageList)
        adapter = Adapter2(messageList!!)
        recView!!.adapter = adapter
    }
}