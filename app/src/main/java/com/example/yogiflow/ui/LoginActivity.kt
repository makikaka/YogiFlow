package com.example.yogiflow.ui

import android.content.Intent
import android.util.Patterns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.yogiflow.R
import com.example.yogiflow.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val btn_login = findViewById(R.id.btn_login) as Button

        btn_login.setOnClickListener {
            doLogin()
        }

        val link_sign_up = findViewById(R.id.link_signup) as TextView

        link_sign_up.setOnClickListener {
            Log.d("SIGNUP", "signup")
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun doLogin() {
        val inputEmail = findViewById(R.id.input_email) as EditText
        val inputPassword = findViewById(R.id.input_password) as EditText
        if (inputEmail.text.toString().isEmpty()){
            inputEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
            inputEmail.setError("Please enter a valid email address")
            inputEmail.requestFocus()
            return
        }

        if (inputPassword.text.toString().isEmpty()) {
            inputPassword.setError("Please enter a password")
            inputPassword.requestFocus()
            return
        }

        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()
        startActivity(Intent(this, MainActivity::class.java))
//        auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        val user = auth.currentUser
//                        updateUI(user)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        updateUI(null)
//                        // ...
//                    }
//
//                    // ...
//                }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
    }

//    private fun updateUI(currentUser: FirebaseUser?) {
//        if (currentUser != null) {
//            startActivity(Intent(this, DashboardActivity::class.java))
//        } else {
//            Toast.makeText(baseContext, "Login failed. Please try again later.",
//                    Toast.LENGTH_SHORT).show()
//        }
//    }
}