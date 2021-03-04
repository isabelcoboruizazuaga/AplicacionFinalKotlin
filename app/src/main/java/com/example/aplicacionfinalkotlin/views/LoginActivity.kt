package com.example.aplicacionfinalkotlin.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacionfinalkotlin.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*


class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var dbReference: DatabaseReference? = null
    private var dbReferenceGoogle: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    var eventListener: ValueEventListener? = null
    var userObject: com.example.aplicacionfinalkotlin.models.User? = null
    var txtEmail: TextView? = null
    var txtPassword: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Layout variables initialization
        txtEmail = findViewById<View>(R.id.textEmail) as EditText
        txtPassword = findViewById<View>(R.id.textPassword) as EditText

        //Initialization of Firebase Authentication
        mAuth = FirebaseAuth.getInstance()

        //Firebase database initialization
        database = FirebaseDatabase.getInstance()
        dbReference = database!!.reference
    }

    fun Login(view: View?) {
        val email = txtEmail!!.text.toString()
        val password = txtPassword!!.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this
                ) { task: Task<AuthResult?> ->
                    //if the login is correct the user is redirected to the main activity
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.errorAutentication), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    //forgotPassword onClick method
    fun forgotPassword(view: View?) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    //Register onClick method
    fun register(view: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    //Google's OnClick Listener
    fun Google(view: View?) {
        val providers = Arrays.asList(
            AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    fun googleDatabase() {
        val user = FirebaseAuth.getInstance().currentUser

        //Database initiaization
        database = FirebaseDatabase.getInstance()
        dbReferenceGoogle = database!!.reference.child("User").child(user!!.uid)

        //The event listened is added to the database
        setEventListener(user)
        dbReferenceGoogle!!.addValueEventListener(eventListener!!)
    }

    fun setEventListener(user: FirebaseUser?) {
        eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    //The data of the logged user are extracted, if the user exist it won't modify them
                    userObject = dataSnapshot.getValue(com.example.aplicacionfinalkotlin.models.User::class.java)
                    val emaiel = userObject!!.email
                    dbReferenceGoogle!!.setValue(userObject)
                    //If the user doesn't exist it will create them
                } catch (e: Exception) {
                    // Name, email address, phone and profile photo Url
                    val name = user!!.displayName
                    val email = user.email
                    val phone = user.phoneNumber
                    val photoUrl = user.photoUrl
                    val uid = user.uid
                    val userObject = com.example.aplicacionfinalkotlin.models.User(uid, email, name, phone, user.providerId)
                    dbReference!!.child("User").child(uid).setValue(userObject)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                //The MainActivity is launched
                googleDatabase()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                //The error message is displayed
                Toast.makeText(this, getResources().getString(R.string.errorLogin), Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 1
    }
}