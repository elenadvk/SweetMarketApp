package com.bitmobileedition.sweetmarket

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
        val buttonSeller: Button = findViewById(R.id.button_seller)

        //обработчик нажатия текста
        linkToAuth.setOnClickListener {
            //создание переменной для перехода с указанием страницы, на которую переходим
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }


        button.setOnClickListener {
            val buyer_login = userLogin.text.toString().trim()
            val buyer_email = userEmail.text.toString().trim()
            val buyer_password = userPass.text.toString().trim()
            val buyer_status = "buyer"

            if (buyer_login.isEmpty() || buyer_email.isEmpty() || buyer_password.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val user = User(buyer_login, buyer_email, buyer_password, buyer_status)
                val db = DbHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Пользователь $buyer_login добавлен", Toast.LENGTH_LONG).show()
            }
        }

        buttonSeller.setOnClickListener {
            val seller_login = userLogin.text.toString().trim()
            val seller_email = userEmail.text.toString().trim()
            val seller_password = userPass.text.toString().trim()
            val seller_status = "seller"

            if (seller_login.isEmpty() || seller_email.isEmpty() || seller_password.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val seller = Seller(seller_login, seller_email, seller_password, seller_status)
                val db = DbHelper(this, null)
                db.addSeller(seller)
                Toast.makeText(this, "Продавец $seller_login добавлен", Toast.LENGTH_LONG).show()
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
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseFirestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // Инициализация Firebase
//        auth = FirebaseAuth.getInstance()
//        db = FirebaseFirestore.getInstance()
//
//        val userLogin: EditText = findViewById(R.id.user_login)
//        val userEmail: EditText = findViewById(R.id.user_email)
//        val userPass: EditText = findViewById(R.id.user_pass)
//
//        val button: Button = findViewById(R.id.button_reg)
//        val linkToAuth: TextView = findViewById(R.id.link_to_auth)
//        val buttonSeller: Button = findViewById(R.id.button_seller)
//
//        // Переход к экрану авторизации
//        linkToAuth.setOnClickListener {
//            val intent = Intent(this, AuthActivity::class.java)
//            startActivity(intent)
//        }
//
//        // Регистрация покупателя
//        button.setOnClickListener {
//            val buyerLogin = userLogin.text.toString().trim()
//            val buyerEmail = userEmail.text.toString().trim()
//            val buyerPassword = userPass.text.toString().trim()
//
//            if (buyerLogin.isEmpty() || buyerEmail.isEmpty() || buyerPassword.isEmpty()) {
//                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//            } else {
//                // Регистрация в Firebase Authentication
//                auth.createUserWithEmailAndPassword(buyerEmail, buyerPassword)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            val user = auth.currentUser
//                            val userData = hashMapOf(
//                                "user_login" to buyerLogin,
//                                "user_email" to buyerEmail,
//                                "user_status" to "buyer"
//                            )
//                            // Сохранение данных о пользователе в Firestore
//                            db.collection("users").document(user!!.uid).set(userData)
//                                .addOnSuccessListener {
//                                    Toast.makeText(this, "Пользователь $buyerLogin добавлен", Toast.LENGTH_LONG).show()
//                                }
//                                .addOnFailureListener { e ->
//                                    Toast.makeText(this, "Ошибка при сохранении данных: ${e.message}", Toast.LENGTH_LONG).show()
//                                }
//                        } else {
//                            // Добавим обработку ошибки
//                            val exception = task.exception?.message ?: "Неизвестная ошибка"
//                            Toast.makeText(this, "Ошибка при регистрации: $exception", Toast.LENGTH_LONG).show()
//                        }
//                    }
//            }
//        }
//
//        // Регистрация продавца
//        buttonSeller.setOnClickListener {
//            val sellerLogin = userLogin.text.toString().trim()
//            val sellerEmail = userEmail.text.toString().trim()
//            val sellerPassword = userPass.text.toString().trim()
//
//            if (sellerLogin.isEmpty() || sellerEmail.isEmpty() || sellerPassword.isEmpty()) {
//                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
//            } else {
//                auth.createUserWithEmailAndPassword(sellerEmail, sellerPassword)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            val user = auth.currentUser
//                            val sellerData = hashMapOf(
//                                "user_login" to sellerLogin,
//                                "user_email" to sellerEmail,
//                                "user_status" to "seller"
//                            )
//                            db.collection("users").document(user!!.uid).set(sellerData)
//                                .addOnSuccessListener {
//                                    Toast.makeText(this, "Продавец $sellerLogin добавлен", Toast.LENGTH_LONG).show()
//                                }
//                                .addOnFailureListener { e ->
//                                    Toast.makeText(this, "Ошибка при сохранении данных: ${e.message}", Toast.LENGTH_LONG).show()
//                                }
//                        } else {
//                            // Добавим обработку ошибки
//                            val exception = task.exception?.message ?: "Неизвестная ошибка"
//                            Toast.makeText(this, "Ошибка при регистрации: $exception", Toast.LENGTH_LONG).show()
//                        }
//                    }
//            }
//        }
//    }
//}

