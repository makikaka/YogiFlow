package com.example.yogiflow.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.yogiflow.R
import com.example.yogiflow.databinding.ActivitySignUpBinding
import com.example.yogiflow.models.AuthToken
import com.example.yogiflow.util.NetworkResult
import com.example.yogiflow.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mainViewModel: MainViewModel
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val btnSignUp = findViewById<Button>(R.id.btn_sign_up)
        val linkLogin = findViewById<TextView>(R.id.link_login)

        mainViewModel.registerResponse.observe(this, Observer<NetworkResult<Boolean>> { token: NetworkResult<Boolean>? ->
            btnSignUp!!.isEnabled = true;

            when(token) {
                is NetworkResult.Success -> {
                    Toast.makeText(this, "Successfully registered", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, token.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    btnSignUp.isEnabled = false;
                }
            }
        })
        
        btnSignUp.setOnClickListener {
            signUpUser()
        }


        linkLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    
    fun signUpUser() {
        val inputEmail = findViewById(R.id.input_email) as EditText?
        val inputPassword = findViewById(R.id.input_password) as EditText?
        if (inputEmail!!.text.toString().isEmpty()){
            inputEmail!!.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail!!.text.toString()).matches()) {
            inputEmail!!.error = "Please enter a valid email address"
            inputEmail!!.requestFocus()
            return
        }

        if (inputPassword!!.text.toString().isEmpty()) {
            inputPassword!!.error = "Please enter a password"
            inputPassword!!.requestFocus()
            return
        }
        var email = inputEmail!!.text.toString()
        var pass = inputPassword!!.text.toString()

        mainViewModel.makeRegisterRequest(email, pass)
    }

}