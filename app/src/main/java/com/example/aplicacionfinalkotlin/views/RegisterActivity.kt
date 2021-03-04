package com.example.aplicacionfinalkotlin.views

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var dbReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var txtName: EditText? = null
    private var txtPhone: EditText? = null
    private var txtEmail: EditText? = null
    private var txtPassword: EditText? = null
    private val txtPhotoUrl: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Layout variables initialization
        txtName = findViewById<View>(R.id.txtName) as EditText
        txtPhone = findViewById<View>(R.id.txtPhone) as EditText
        txtEmail = findViewById<View>(R.id.txtEmail) as EditText
        txtPassword = findViewById<View>(R.id.txtPassword) as EditText

        //Initialization of Firebase Authentication
        mAuth = FirebaseAuth.getInstance()
        //Firebase database initialization
        database = FirebaseDatabase.getInstance()
        dbReference = database!!.reference
    }

    //This method will be launched when the user press the Register button
    private fun createNewAccount() {
        val name = txtName!!.text.toString()
        val phone = txtPhone!!.text.toString()
        val email = txtEmail!!.text.toString()
        val password = txtPassword!!.text.toString()

        //If all the text fields are complete the user is register
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this
                ) { task: Task<AuthResult?> ->
                    //if the account is created correctly the user is redirected to the main activity
                    if (task.isSuccessful) {
                        //The user is sent a verification Email
                        val user = mAuth!!.currentUser
                        verifyEmail(user)

                        //The user is created and added to the database
                        val uid = user!!.uid
                        val userObject = User(uid, email, name, phone, user.providerId)
                        dbReference!!.child("User").child(uid).setValue(userObject)
                        updateUI(user)
                        //if it fails the user is informed
                    } else {
                        updateUI(null)
                    }
                }
        }
    }

    //Method to redirect the user to the login activity. If the register failed it won't redirect the user
    private fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, getResources().getString(R.string.successSignUp), Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(applicationContext, getResources().getString(R.string.errorSignUp), Toast.LENGTH_SHORT)
        }
    }

    //Method for the verification of the user email
    private fun verifyEmail(user: FirebaseUser?) {
        user!!.sendEmailVerification().addOnCompleteListener(this
        ) { task: Task<Void?> ->
            if (task.isSuccessful) {
                Toast.makeText(this,  getResources().getString(R.string.succesEmail), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,  getResources().getString(R.string.errorEmail), Toast.LENGTH_LONG).show()
            }
        }
    }

    //Register onClick method
    fun Register(view: View?) {
        createNewAccount()
    }
}