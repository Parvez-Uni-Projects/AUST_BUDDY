package com.example.homepage

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.homepage.databinding.ActivitySignUpBinding
import com.example.homepage.onBoarding.OnBoarding2
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    //private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""
    private var a = 0

    //**** BY DEFAULTT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseAuth = FirebaseAuth.getInstance()

        val signUpButton = findViewById<Button>(R.id.signupBtn)
        val text1 = findViewById<TextView>(R.id.text1)
        signUpButton.setOnClickListener {


            email = findViewById<TextView>(R.id.emailEt).text.toString()
            password = findViewById<TextView>(R.id.passwordEt).text.toString()
            val situation = validateData(email, password)

            if (situation == "OK") {
                Toast.makeText(applicationContext, "EVERYTHING ALL RIGHT", Toast.LENGTH_SHORT)
                    .show()
                fireBaseSignup()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(applicationContext, situation, Toast.LENGTH_SHORT).show()
            }

        }

        text1.setOnClickListener {

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validateData(emailf: String, passwordf: String): String {

        email = emailf
        password = passwordf
        var situation = "NOT POSSIBLE";

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            situation = "Invalid email format"
        else if (email.length >= 35)
            situation = "Too long characters"
        else if (email.contains("@gmail.com", ignoreCase = true) || email.contains(
                "@yahoo.com",
                ignoreCase = true
            )
        )
            situation = "Provide your edu mail"
        else if (TextUtils.isEmpty(password))
            situation = "Enter a password"
        else if (password.length <= 6)
            situation = "Password is too short"
        else if (password.contains("@") || password.contains("#") || password.contains("%")
            || password.contains("$") || password.contains("*")
        )
            situation = "OK"
        else if (a != 1)
            situation = "Give a special character such as @,$,#.."
        else
            situation = "GOD KNOWS WHAT HAPPEND!"

        return situation

    }


    private fun fireBaseSignup() {
        // progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {

                //progressDialog.dismiss()
                checkMail()
                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()

            } else {
                //progressDialog.dismiss()
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkMail() {

        firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Toast.makeText(
                    this,
                    "Verification mail has been sent on this email $email",
                    Toast.LENGTH_SHORT
                ).show()
                //firebaseAuth.signOut()
                //finish()
            } else {
                Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show()


            }


        }


    }

}