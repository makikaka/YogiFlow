package com.example.yogiflow.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.yogiflow.R
import com.example.yogiflow.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        val btnSignUp = findViewById<Button>(R.id.btn_sign_up)
        val linkLogin = findViewById<TextView>(R.id.link_login)

        btnSignUp.setOnClickListener {
            signUpUser()
        }


        linkLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    
    fun signUpUser() {
        val inputEmail = findViewById(R.id.input_email) as EditText
        val inputPassword = findViewById(R.id.input_password) as EditText
        if (inputEmail.text.toString().isEmpty()){
            inputEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
            inputEmail.error = "Please enter a valid email address"
            inputEmail.requestFocus()
            return
        }

        if (inputPassword.text.toString().isEmpty()) {
            inputPassword.error = "Please enter a password"
            inputPassword.requestFocus()
            return
        }
        var email = inputEmail.text.toString()
        var pass = inputPassword.text.toString()



//        auth.createUserWithEmailAndPassword(email, pass)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Toast.makeText(baseContext, "Sign Up successful. Login using the account you just created!",
//                        Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, LoginActivity::class.java))
//                    finish()
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(baseContext, "Sign Up failed. Try again after some time",
//                        Toast.LENGTH_SHORT).show()
//                }
//
//                // ...
//            }
    }

}