package com.bitmobileedition.sweetmarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val addressTextView: TextView = findViewById(R.id.address_text)
        val paymentTextView: TextView = findViewById(R.id.payment_text)
        val placeOrderButton: Button = findViewById(R.id.place_order_button)

        // Инициализация отображения адреса и способа оплаты
        addressTextView.text = "Адрес доставки: Улица, дом"
        paymentTextView.text = "Способ оплаты: Карта"

        placeOrderButton.setOnClickListener {
            // Обработка оформления заказа
            // Очистить корзину после оформления заказа
            val sp = getSharedPreferences("preferences", MODE_PRIVATE)
            sp.edit().remove("cart").apply()

            // Открыть экран подтверждения
            val intent = Intent(this, ConfirmationActivity::class.java)
            startActivity(intent)
        }
    }
}
