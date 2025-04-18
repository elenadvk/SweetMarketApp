package com.bitmobileedition.sweetmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)

        val linkToReg: TextView = findViewById(R.id.link_to_reg)
        val authAsSeller: TextView = findViewById(R.id.auth_as_seller)

        // Переход к экрану регистрации
        linkToReg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val db = DbHelper(this, null)
                val isAuth = db.getUser(login, pass)

                if (isAuth) {
                    Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()
                    userLogin.text.clear()
                    userPass.text.clear()

                    // Переход к основной активности после авторизации покупателя
                    val intent = Intent(this, ItemsActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Пользователь $login не авторизован", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Авторизация как продавец
        authAsSeller.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val db = DbHelper(this, null)
                val isAuth = db.getSeller(login, pass)

                if (isAuth) {
                    Toast.makeText(this, "Продавец $login авторизован", Toast.LENGTH_LONG).show()
                    userLogin.text.clear()
                    userPass.text.clear()

                    // Переход к основной активности после авторизации продавца
                    val intent = Intent(this, ItemsActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Продавец $login не авторизован", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
























//package com.bitmobileedition.sweetmarket
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore

//авторизация через Firebase
//class AuthActivity : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseFirestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_auth)
//
//        auth = FirebaseAuth.getInstance()
//        db = FirebaseFirestore.getInstance()
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
//        // Авторизация покупателя
//        button.setOnClickListener {
//            val login = userLogin.text.toString().trim()
//            val pass = userPass.text.toString().trim()
//
//            if (login.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//            } else {
//                auth.signInWithEmailAndPassword(login, pass)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            val user = auth.currentUser
//                            // Получение данных о пользователе из Firestore
//                            db.collection("users").document(user!!.uid).get()
//                                .addOnSuccessListener { document ->
//                                    val userStatus = document.getString("user_status")
//                                    if (userStatus == "buyer" || userStatus == "seller") {
//                                        Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()
//                                        val intent = Intent(this, ItemsActivity::class.java)
//                                        startActivity(intent)
//                                    } else {
//                                        Toast.makeText(this, "Неверный статус пользователя", Toast.LENGTH_LONG).show()
//                                    }
//                                }
//                                .addOnFailureListener {
//                                    Toast.makeText(this, "Ошибка при получении данных пользователя", Toast.LENGTH_LONG).show()
//                                }
//                        } else {
//                            Toast.makeText(this, "Ошибка при авторизации", Toast.LENGTH_LONG).show()
//                        }
//                    }
//            }
//        }
//
//        // Авторизация как продавец
//        authAsSeller.setOnClickListener {
//            val login = userLogin.text.toString().trim()
//            val pass = userPass.text.toString().trim()
//
//            if (login.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//            } else {
//                auth.signInWithEmailAndPassword(login, pass)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            val user = auth.currentUser
//                            db.collection("users").document(user!!.uid).get()
//                                .addOnSuccessListener { document ->
//                                    val userStatus = document.getString("user_status")
//                                    if (userStatus == "seller") {
//                                        Toast.makeText(this, "Продавец $login авторизован", Toast.LENGTH_LONG).show()
//                                        val intent = Intent(this, ItemsActivity::class.java)
//                                        startActivity(intent)
//                                    } else {
//                                        Toast.makeText(this, "Неверный статус пользователя", Toast.LENGTH_LONG).show()
//                                    }
//                                }
//                                .addOnFailureListener {
//                                    Toast.makeText(this, "Ошибка при получении данных продавца", Toast.LENGTH_LONG).show()
//                                }
//                        } else {
//                            Toast.makeText(this, "Ошибка при авторизации", Toast.LENGTH_LONG).show()
//                        }
//                    }
//            }
//        }
//    }
//}




