package com.bitmobileedition.sweetmarket

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.tools.build.jetifier.core.utils.Log
import kotlinx.coroutines.launch


class OtpActivity : AppCompatActivity() {
    private lateinit var otpInput: EditText
    private lateinit var verifyButton: Button
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        otpInput = findViewById(R.id.otp_code_input)
        verifyButton = findViewById(R.id.otp_verify_button)

        // Получаем userId из Intent или SharedPreferences
        userId = intent.getStringExtra("userId") ?: getUserIdFromPreferences()
        Log.e("gotUserId", "Получен такой $userId")

        // Если userId не получен, выводим ошибку
        if (userId == null) {
            Toast.makeText(this, "Ошибка: не удалось получить userId", Toast.LENGTH_SHORT).show()
            return
        }

        verifyButton.setOnClickListener {
            val otpCode = otpInput.text.toString()
            Log.e("otpInput", "Был введен код $otpCode для пользователя $userId")

            if (otpCode.length != 6) {
                Toast.makeText(this, "Введите корректный код", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Отправляем запрос на сервер для проверки OTP
            lifecycleScope.launch {
                try {
                    // Создаем запрос на верификацию OTP
                    val response = RetrofitInstance.api.verifyOtp(userId!!, otpCode)
                    Log.e("responseOTP", "$response")

                    if (response.isSuccessful) {
                        val otpResponse = response.body()
                        if (otpResponse != null && otpResponse.isSuccess) {
                            Toast.makeText(this@OtpActivity, "Email подтвержден!", Toast.LENGTH_SHORT).show()
                            saveUserId(userId!!)
                            startActivity(Intent(this@OtpActivity, AuthActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@OtpActivity, "Неверный код", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@OtpActivity, "Ошибка сети", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@OtpActivity, "Ошибка: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    Log.e("localizedMessage", "${e.localizedMessage}")
                }
            }
        }
    }

    private fun getUserIdFromPreferences(): String? {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

    private fun saveUserId(userId: String) {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("user_id", userId).apply()
    }
}