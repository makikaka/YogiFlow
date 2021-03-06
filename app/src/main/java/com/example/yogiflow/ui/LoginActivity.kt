package com.example.yogiflow.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.yogiflow.R
import com.example.yogiflow.databinding.ActivityLoginBinding
import com.example.yogiflow.models.AuthToken
import com.example.yogiflow.util.NetworkResult
import com.example.yogiflow.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mainViewModel: MainViewModel
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val loginButton = findViewById(R.id.btn_login) as Button?
        mainViewModel.loginResponse.observe(this, Observer<NetworkResult<AuthToken>> { token: NetworkResult<AuthToken>? ->
            loginButton!!.isEnabled = true;

            when(token) {
                is NetworkResult.Success -> {
                    val prefs: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                    val edit: SharedPreferences.Editor = prefs.edit()

                    try {
                        val saveToken: String = token!!.data!!.authToken
                        edit.putString("token", saveToken)
                        Log.i("Login", saveToken)
                        edit.apply()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, token.message, LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    loginButton.isEnabled = false;
                }
            }
        })

        loginButton!!.setOnClickListener {
           doLogin()
        }

        val link_sign_up = findViewById(R.id.link_signup) as TextView?

        link_sign_up!!.setOnClickListener {
            Log.d("SIGNUP", "signup")
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun doLogin() {
        val inputEmail = findViewById(R.id.input_email) as EditText?
        val inputPassword = findViewById(R.id.input_password) as EditText?
        if (inputEmail!!.text.toString().isEmpty()){
            inputEmail!!.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
            inputEmail!!.setError("Please enter a valid email address")
            inputEmail!!.requestFocus()
            return
        }

        if (inputPassword!!.text.toString().isEmpty()) {
            inputPassword!!.setError("Please enter a password")
            inputPassword!!.requestFocus()
            return
        }

        val email = inputEmail!!.text.toString()
        val password = inputPassword!!.text.toString()
//        startActivity(Intent(this, MainActivity::class.java))


        mainViewModel.makeLoginRequest(email, password);

    // SHARED PREFERENCES

    }

    private fun makeLoginRequest() {

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
