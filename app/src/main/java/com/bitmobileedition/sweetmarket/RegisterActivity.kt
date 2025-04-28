//package com.bitmobileedition.sweetmarket
//
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//import android.widget.Toast
//
//class RegisterActivity : AppCompatActivity() {
//
//    private lateinit var viewModel: AuthViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//
//        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
//
//        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
//        val emailEditText = findViewById<EditText>(R.id.emailEditText)
//        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
//        val registerButton = findViewById<Button>(R.id.registerButton)
//
//        registerButton.setOnClickListener {
//            val username = usernameEditText.text.toString()
//            val email = emailEditText.text.toString()
//            val password = passwordEditText.text.toString()
//
//            viewModel.register(username, email, password)
//        }
//
//        viewModel.registerResponse.observe(this, Observer {
//            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
//            // Перейти к следующей активности
//        })
//
//        viewModel.errorMessage.observe(this, Observer { errorMessage ->
//            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//        })
//    }
//}
