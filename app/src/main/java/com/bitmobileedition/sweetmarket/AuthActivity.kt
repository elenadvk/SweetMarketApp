//авторизация через сервер и PG
package com.bitmobileedition.sweetmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.tools.build.jetifier.core.utils.Log
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userEmail: EditText = findViewById(R.id.user_login_auth)
//        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)

        val linkToReg: TextView = findViewById(R.id.link_to_reg)
        val authAsSeller: TextView = findViewById(R.id.auth_as_seller)

        // Переход к экрану регистрации
        linkToReg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


//        button.setOnClickListener {
//            val email = userEmail.text.toString().trim()
//            val pass = userPass.text.toString().trim()
//            val userType = "customer"
//
//            if (email.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val loginRequest = LoginRequest(email, pass, userType.lowercase())
//
//            lifecycleScope.launch {
//                try {
//                    val response = RetrofitInstance.api.loginUser(loginRequest)
//                    if (response.isSuccessful) {
//                        val user = response.body()?.user
//                        val userId = user?.id
//                        if (userId != null) {
//                            saveUserId(userId)  // Сохраняем userId
//
//                            val isVerified = user.isVerified ?: false
//                            if (!isVerified) {
//                                // Аккаунт не подтвержден, открываем экран OTP
//                                Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
//                                val intent = Intent(this@AuthActivity, OtpActivity::class.java)
//                                intent.putExtra("userId", userId)
//                                startActivity(intent)
//                                return@launch
//                            }
//
//                            // Если аккаунт подтвержден
//                            val token = response.body()?.accessToken
//                            saveAuthToken(token)
//                            Toast.makeText(this@AuthActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
//                            startActivity(Intent(this@AuthActivity, ItemsActivity::class.java))
//                            finish()
//                        } else {
//                            // Если userId не найден
//                            Toast.makeText(this@AuthActivity, "Ошибка при получении userId", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        val errorBody = response.errorBody()?.string()
//                        Log.e("LoginError", "Code: ${response.code()}, Body: $errorBody")
//
//                        if (errorBody != null) {
//                            if (errorBody?.contains("Account is not verified") == true || errorBody.contains("is not Exist")) {
//                                // Если аккаунт не подтвержден, проверяем userId
//                                val userId = getSavedUserId()
//                                if (userId == null) {
//                                    Toast.makeText(this@AuthActivity, "UserId не найден. Попробуйте снова", Toast.LENGTH_SHORT).show()
//                                    return@launch
//                                }
//
//                                Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
//                                val intent = Intent(this@AuthActivity, OtpActivity::class.java)
//                                intent.putExtra("userId", userId)
//                                startActivity(intent)
//                            } else {
//                                Toast.makeText(this@AuthActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(this@AuthActivity, "Ошибка сети: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        button.setOnClickListener {
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()
            val userType = "customer"

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, pass, userType.lowercase())

            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.loginUser(loginRequest)
                    if (response.isSuccessful) {
                        val user = response.body()?.user
                        val userId = user?.id
                        val isVerified = user?.isVerified ?: false

                        saveUserId(userId)

                        if (!isVerified) {
                            if (response.code() == 400 && response.errorBody()?.string()?.contains("Account is not verified") == true) {
                                val userId = getSavedUserId() // или из response.body()?.user?.id
                                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                            }
//                            Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this@AuthActivity, OtpActivity::class.java)
//                            intent.putExtra("userId", userId)
//                            startActivity(intent)
//                            return@launch
                        }

                        val token = response.body()?.accessToken
                        saveAuthToken(token)
                        Toast.makeText(this@AuthActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AuthActivity, ItemsActivity::class.java))
                        finish()
                    } else {
                        if (response.code() == 400 && response.errorBody()?.string()?.contains("Account is not verified") == true) {
                            // Перенаправление на экран подтверждения
                            val userId = getSavedUserId() // или из response.body()?.user?.id
                            val intent = Intent(this@AuthActivity, ItemsActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                        }
                        val userId = getSavedUserId() // или из response.body()?.user?.id
                        val intent = Intent(this@AuthActivity, ItemsActivity::class.java)
                        intent.putExtra("userId", userId)
                        startActivity(intent)
                        Toast.makeText(this@AuthActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AuthActivity, "Ошибка сети: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }

//        button.setOnClickListener {
//            val email = userEmail.text.toString().trim()
//            val pass = userPass.text.toString().trim()
//            val userType = "customer"
//
//            if (email.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val loginRequest = LoginRequest(email, pass, userType.lowercase())
//
//            lifecycleScope.launch {
//                try {
//                    val response = RetrofitInstance.api.loginUser(loginRequest)
//                    if (response.isSuccessful) {
//                        val user = response.body()?.user
//                        val userId = user?.id
//                        val isVerified = user?.isVerified ?: false
//
//                        saveUserId(userId)
//
//                        if (!isVerified) {
//                            Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this@AuthActivity, OtpActivity::class.java)
//                            intent.putExtra("userId", userId)
//                            startActivity(intent)
//                            return@launch
//                        }
//
//                        val token = response.body()?.accessToken
//                        saveAuthToken(token)
//                        Toast.makeText(this@AuthActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@AuthActivity, ItemsActivity::class.java))
//                        finish()
//                    } else {
//                        if (response.code() == 400) {
//                            Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
//                            val userId = getSavedUserId()
//                            if (userId != null) {
//                                val intent = Intent(this@AuthActivity, OtpActivity::class.java)
//                                intent.putExtra("userId", userId)
//                                startActivity(intent)
//                            } else {
//                                Toast.makeText(this@AuthActivity, "UserId не найден", Toast.LENGTH_SHORT).show()
//                            }
//                        } else {
//                            Toast.makeText(this@AuthActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(this@AuthActivity, "Ошибка сети: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        // Авторизация как покупатель
//        button.setOnClickListener {
//            val email = userEmail.text.toString().trim()
//            val pass = userPass.text.toString().trim()
//            val userType = "customer"
//
//            if (email.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val loginRequest = LoginRequest(email, pass, userType.lowercase())
//
//            lifecycleScope.launch {
//                try {
//                    val response = RetrofitInstance.api.loginUser(loginRequest)
//                    if (response.isSuccessful) {
//                        val userId = response.body()?.user?.id
//                        saveUserId(userId) // Сохраняем userId, даже если уже верифицирован
//
//                        val isVerified = response.body()?.user?.isVerified ?: false
//                        if (!isVerified) {
//                            val userId = response.body()?.user?.id
//                            saveUserId(userId) // Обязательно сохрани userId перед переходом в OtpActivity
//
//                            Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this@AuthActivity, OtpActivity::class.java)
//                            intent.putExtra("userId", response.body()?.user?.id)
//                            startActivity(intent)
//                            return@launch
//                        }
//
//                        val token = response.body()?.accessToken
//                        saveAuthToken(token)
//                        Toast.makeText(this@AuthActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@AuthActivity, ItemsActivity::class.java))
//                        finish()
//                    } else {
//                        val errorBody = response.errorBody()?.string()
//                        Log.e("LoginError", "Code: ${response.code()}, Body: $errorBody")
//
//                        if (errorBody?.contains("Account is not verified") == true || errorBody!!.contains("is not Exist")) {
//                            Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
//
//                            val userId = getSavedUserId()
//                            if (userId == null) {
//                                Toast.makeText(this@AuthActivity, "UserId не найден", Toast.LENGTH_SHORT).show()
//                                return@launch
//                            }
//                            val intent = Intent(this@AuthActivity, OtpActivity::class.java)
//                            intent.putExtra("userId", userId)
//                            startActivity(intent)
//
//                        } else {
//                            Toast.makeText(this@AuthActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
////                    if (response.isSuccessful) {
////                        val isVerified = response.body()?.user?.isVerified ?: false
////                        if (!isVerified) {
////                            Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
////                            val intent = Intent(this@AuthActivity, OtpActivity::class.java)
////                            intent.putExtra("userId", response.body()?.user?.id)
////                            startActivity(intent)
////                            return@launch
////                        }
////
////                        val token = response.body()?.accessToken
////                        saveAuthToken(token)
////                        Toast.makeText(this@AuthActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
////                        startActivity(Intent(this@AuthActivity, ItemsActivity::class.java))
////                        finish()
////                    } else {
////                        val errorCode = response.code()
////                        val errorBody = response.errorBody()?.string()
////                        Log.e("LoginError", "Code: $errorCode, Body: $errorBody")  // добавь это
////                        Toast.makeText(this@AuthActivity, "Ошибка авторизации: $errorCode", Toast.LENGTH_SHORT).show()
////                    }
//////                    val response = RetrofitInstance.api.loginUser(loginRequest)
//////                    if (response.isSuccessful) {
//////                        val isVerified = response.body()?.user?.isVerified ?: false
//////                        if (!isVerified) {
//////                            Toast.makeText(this@AuthActivity, "Аккаунт не подтвержден. Введите OTP", Toast.LENGTH_SHORT).show()
//////                            // Переход на экран ввода OTP
//////                            val intent = Intent(this@AuthActivity, OtpActivity::class.java)
//////                            intent.putExtra("userId", response.body()?.user?.id)
//////                            startActivity(intent)
//////                            return@launch
//////                        }
//////
//////                        val token = response.body()?.accessToken
//////                        // Сохраняем токен в SharedPreferences для дальнейшего использования
//////                        saveAuthToken(token)
//////                        Toast.makeText(this@AuthActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
//////
//////                        // Переход на главную страницу после авторизации
//////                        startActivity(Intent(this@AuthActivity, ItemsActivity::class.java))
//////                        finish()  // Закрываем экран авторизации
//////                    } else {
//////                        val errorBody = response.errorBody()?.string()
////////                        Toast.makeText(this@AuthActivity, "Ошибка авторизации: $errorBody", Toast.LENGTH_LONG).show()
//////                        Toast.makeText(this@AuthActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
//////                    }
//                } catch (e: Exception) {
//                    Toast.makeText(this@AuthActivity, "Ошибка сети: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        // Авторизация как продавец
        authAsSeller.setOnClickListener {
            val login = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val db = DbHelper(this, null)
                val isAuth = db.getSeller(login, pass)

                if (isAuth) {
                    Toast.makeText(this, "Продавец $login авторизован", Toast.LENGTH_LONG).show()
                    userEmail.text.clear()
                    userPass.text.clear()

                    // Переход к основной активности после авторизации продавца
                    val intent = Intent(this, SellerActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Продавец $login не авторизован", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Метод для сохранения токена в SharedPreferences
    private fun saveAuthToken(token: String?) {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }
    private fun getSavedUserId(): String? {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }
    private fun saveUserId(userId: String?) {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", userId)
        editor.apply()
    }
}

//авторизация через DBHelper
//package com.bitmobileedition.sweetmarket
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.lifecycle.lifecycleScope
//import kotlinx.coroutines.launch
//
//
//class AuthActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_auth)
//
//        val userLogin: EditText = findViewById(R.id.user_login_auth)
//        val userPass: EditText = findViewById(R.id.user_pass_auth)
//        val button: Button = findViewById(R.id.button_auth)
//
//        val linkToReg: TextView = findViewById(R.id.link_to_reg)
//        val authAsSeller: TextView = findViewById(R.id.auth_as_seller)
//
//        // Переход к экрану регистрации
//        linkToReg.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//
//        button.setOnClickListener {
//            val login = userLogin.text.toString().trim()
//            val pass = userPass.text.toString().trim()
//
//            if (login.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//            } else {
//                val db = DbHelper(this, null)
//                val isAuth = db.getUser(login, pass)
//
//                if (isAuth) {
//                    Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()
//                    userLogin.text.clear()
//                    userPass.text.clear()
//
//                    // Переход к основной активности после авторизации покупателя
//                    val intent = Intent(this, ItemsActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(this, "Пользователь $login не авторизован", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//
//
//
//        // Авторизация как продавец
//        authAsSeller.setOnClickListener {
//            val login = userLogin.text.toString().trim()
//            val pass = userPass.text.toString().trim()
//
//            if (login.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//            } else {
//                val db = DbHelper(this, null)
//                val isAuth = db.getSeller(login, pass)
//
//                if (isAuth) {
//                    Toast.makeText(this, "Продавец $login авторизован", Toast.LENGTH_LONG).show()
//                    userLogin.text.clear()
//                    userPass.text.clear()
//
//                    // Переход к основной активности после авторизации продавца
//                    val intent = Intent(this, SellerActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(this, "Продавец $login не авторизован", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//}


//
//
//
////class AuthActivity : AppCompatActivity() {
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_auth)
////
////        val userLogin: EditText = findViewById(R.id.user_login_auth)
//////        val userEmail: EditText = findViewById(R.id.user_email_auth)
////        val userPass: EditText = findViewById(R.id.user_pass_auth)
////        val button: Button = findViewById(R.id.button_auth)
////
////        val linkToReg: TextView = findViewById(R.id.link_to_reg)
////        val authAsSeller: TextView = findViewById(R.id.auth_as_seller)
////
//////        val registrationRequest = RegistrationRequest(email, password, "customer")
//////        val response = apiService.registerUser(registrationRequest)
//////        val loginRequest = LoginRequest(email, password, "customer")
//////        val response = apiService.loginUser(loginRequest)
////
////
////        // Переход к экрану регистрации
////        linkToReg.setOnClickListener {
////            val intent = Intent(this, MainActivity::class.java)
////            startActivity(intent)
////        }
////
////        button.setOnClickListener {
////            val email = userLogin.text.toString().trim()
////            val pass = userPass.text.toString().trim()
////            val userType = "customer"
////
////            if (email.isEmpty() || pass.isEmpty()) {
////                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
////                return@setOnClickListener
////            }
////
////            val loginRequest = LoginRequest(email, pass, userType)
////
////            lifecycleScope.launch {
////                try {
////                    val response = RetrofitInstance.api.loginUser(loginRequest)
////                    if (response.isSuccessful) {
////                        val token = response.body()?.accessToken
////                        // Сохрани токен в SharedPreferences
////                        Toast.makeText(this@AuthActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
////                        startActivity(Intent(this@AuthActivity, ItemsActivity::class.java))
////                    } else {
////                        Toast.makeText(this@AuthActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
////                    }
////                } catch (e: Exception) {
////                    Toast.makeText(this@AuthActivity, "Ошибка сети: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
////                }
////            }
////        }
////
////
//////            lifecycleScope.launch {
//////                try {
//////                    val response = RetrofitInstance.api.loginUser(loginRequest)
//////                    if (response.isSuccessful) {
//////                        val token = response.body()?.accessToken
//////                        // Сохрани токен в SharedPreferences
//////                        Toast.makeText(this@AuthActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
//////                        startActivity(Intent(this@AuthActivity, ItemsActivity::class.java))
//////                    } else {
//////                        Toast.makeText(this@AuthActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
//////                    }
//////                } catch (e: Exception) {
//////                    Toast.makeText(this@AuthActivity, "Ошибка сети: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//////                }
//////            }
//////        }
////
//////        button.setOnClickListener {
//////            val login = userLogin.text.toString().trim()
//////            val pass = userPass.text.toString().trim()
//////
//////            if (login.isEmpty() || pass.isEmpty()) {
//////                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//////            } else {
//////                val db = DbHelper(this, null)
//////                val isAuth = db.getUser(login, pass)
//////
//////                if (isAuth) {
//////                    Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()
//////                    userLogin.text.clear()
//////                    userPass.text.clear()
//////
//////                    // Переход к основной активности после авторизации покупателя
//////                    val intent = Intent(this, ItemsActivity::class.java)
//////                    startActivity(intent)
//////                } else {
//////                    Toast.makeText(this, "Пользователь $login не авторизован", Toast.LENGTH_LONG).show()
//////                }
//////            }
//////        }
////
////
////
////
////        // Авторизация как продавец
////        authAsSeller.setOnClickListener {
////            val login = userLogin.text.toString().trim()
////            val pass = userPass.text.toString().trim()
////
////            if (login.isEmpty() || pass.isEmpty()) {
////                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
////            } else {
////                val db = DbHelper(this, null)
////                val isAuth = db.getSeller(login, pass)
////
////                if (isAuth) {
////                    Toast.makeText(this, "Продавец $login авторизован", Toast.LENGTH_LONG).show()
////                    userLogin.text.clear()
////                    userPass.text.clear()
////
////                    // Переход к основной активности после авторизации продавца
////                    val intent = Intent(this, SellerActivity::class.java)
////                    startActivity(intent)
////                } else {
////                    Toast.makeText(this, "Продавец $login не авторизован", Toast.LENGTH_LONG).show()
////                }
////            }
////        }
////    }
////}
