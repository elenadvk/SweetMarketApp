//регистрация через сервер и PG
package com.bitmobileedition.sweetmarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPass: EditText = findViewById(R.id.user_pass)

        val button: Button = findViewById(R.id.button_reg)

        //переменная, отвечающая за переход между окнами авторизации и регистрации
        val linkToAuth: TextView = findViewById(R.id.link_to_auth)

        //переменная, отвечающая за регистрацию в качетсве продавца
//        val buttonSeller: Button = findViewById(R.id.button_seller)

        //обработчик нажатия текста
        linkToAuth.setOnClickListener {
            //создание переменной для перехода с указанием страницы, на которую переходим
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()
            val userType = "customer"

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registerRequest = RegisterRequest(email, pass, userType)
            Log.e("1got", "$registerRequest")
            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.registerUser(registerRequest)

                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            val userId = responseBody.data.id
                            Log.d("UserId", userId ?: "UserId is null")
                            Log.d("ResponseString", responseBody.toString())  // Выведет весь JSON

                            if (userId != null) {
                                Log.e("mainGotUserId", "С сервера получен $userId")
                                saveUserId(userId)

                                val intent = Intent(this@MainActivity, OtpActivity::class.java).apply {
                                    putExtra("userId", userId)
                                }
                                Log.e("sentUserId", "Sent $userId")
                                startActivity(intent)
                            } else {
                                Log.e("mainGotUserId", "UserId is null in response $responseBody")
                                Toast.makeText(this@MainActivity, "Ошибка: получен пустой userId", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("mainGotUserId", "Ошибка получения тела ответа")
                            Toast.makeText(this@MainActivity, "Ошибка: пустой ответ от сервера", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("Registration Error", "Response: ${response.message()}")
                        Toast.makeText(this@MainActivity, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("Network Error", "Error: ${e.localizedMessage}")
                    Toast.makeText(this@MainActivity, "Ошибка сети: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }


//        buttonSeller.setOnClickListener {
//            val email = userEmail.text.toString().trim()
//            val pass = userPass.text.toString().trim()
//            val userType = "seller"
//
//            if (email.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val registerRequest = RegisterRequest(email, pass, userType)
//            Log.e("1got", "$registerRequest")
//            lifecycleScope.launch {
//                try {
//                    val response = RetrofitInstance.api.registerUser(registerRequest)
//
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//
//                        if (responseBody != null) {
//                            val userId = responseBody.data.id
//                            Log.d("UserId", userId ?: "UserId is null")
//                            Log.d("ResponseString", responseBody.toString())  // Выведет весь JSON
//
//                            if (userId != null) {
//                                Log.e("mainGotUserId", "С сервера получен $userId")
//                                saveUserId(userId)
//
//                                val intent = Intent(this@MainActivity, OtpActivity::class.java).apply {
//                                    putExtra("userId", userId)
//                                }
//                                Log.e("sentUserId", "Sent $userId")
//                                startActivity(intent)
//                            } else {
//                                Log.e("mainGotUserId", "UserId is null in response $responseBody")
//                                Toast.makeText(this@MainActivity, "Ошибка: получен пустой userId", Toast.LENGTH_SHORT).show()
//                            }
//                        } else {
//                            Log.e("mainGotUserId", "Ошибка получения тела ответа")
//                            Toast.makeText(this@MainActivity, "Ошибка: пустой ответ от сервера", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        Log.e("Registration Error", "Response: ${response.message()}")
//                        Toast.makeText(this@MainActivity, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
//                    }
//                } catch (e: Exception) {
//                    Log.e("Network Error", "Error: ${e.localizedMessage}")
//                    Toast.makeText(this@MainActivity, "Ошибка сети: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

    }
    private fun saveUserId(userId: String?) {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", userId)
        editor.apply()
    }
}



//регистрация через DBHelper
// package com.bitmobileedition.sweetmarket
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import kotlinx.coroutines.launch
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val userLogin: EditText = findViewById(R.id.user_login)
//        val userEmail: EditText = findViewById(R.id.user_email)
//        val userPass: EditText = findViewById(R.id.user_pass)
//
//        val button: Button = findViewById(R.id.button_reg)
//
//        //переменная, отвечающая за переход между окнами авторизации и регистрации
//        val linkToAuth: TextView = findViewById(R.id.link_to_auth)
//
//        //переменная, отвечающая за регистрацию в качетсве продавца
//        val buttonSeller: Button = findViewById(R.id.button_seller)
//
//        //обработчик нажатия текста
//        linkToAuth.setOnClickListener {
//            //создание переменной для перехода с указанием страницы, на которую переходим
//            val intent = Intent(this, AuthActivity::class.java)
//            startActivity(intent)
//        }
//
//
//        button.setOnClickListener {
//            val buyer_login = userLogin.text.toString().trim()
//            val buyer_email = userEmail.text.toString().trim()
//            val buyer_password = userPass.text.toString().trim()
//            val buyer_status = "buyer"
//
//            if (buyer_login.isEmpty() || buyer_email.isEmpty() || buyer_password.isEmpty()) {
//                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//            } else {
//                val user = User(buyer_login, buyer_email, buyer_password, buyer_status)
//                val db = DbHelper(this, null)
//                db.addUser(user)
//                Toast.makeText(this, "Пользователь $buyer_login добавлен", Toast.LENGTH_LONG).show()
//            }
//        }
//
//
//
//        buttonSeller.setOnClickListener {
//            val seller_login = userLogin.text.toString().trim()
//            val seller_email = userEmail.text.toString().trim()
//            val seller_password = userPass.text.toString().trim()
//            val seller_status = "seller"
//
//            if (seller_login.isEmpty() || seller_email.isEmpty() || seller_password.isEmpty()) {
//                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//            } else {
//                val seller = Seller(seller_login, seller_email, seller_password, seller_status)
//                val db = DbHelper(this, null)
//                db.addSeller(seller)
//                Toast.makeText(this, "Продавец $seller_login добавлен", Toast.LENGTH_LONG).show()
//            }
//        }
//
//    }
//}


