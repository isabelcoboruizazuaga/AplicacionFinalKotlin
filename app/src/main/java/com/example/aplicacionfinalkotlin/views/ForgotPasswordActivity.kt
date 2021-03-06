package com.example.aplicacionfinalkotlin.views

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacionfinalkotlin.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = FirebaseAuth.getInstance()
    }

    fun send(view: View?) {
        val txtEmail = findViewById<View>(R.id.txtEmail) as EditText
        val email = txtEmail.text.toString()
        if (!TextUtils.isEmpty(email)) {
            auth!!.sendPasswordResetEmail(email).addOnCompleteListener(this
            ) { task: Task<Void?> ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    Toast.makeText(this, getResources().getString(R.string.errorEmail), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}