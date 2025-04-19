package com.bitmobileedition.sweetmarket

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val confirmationText: TextView = findViewById(R.id.confirmation_text)
        confirmationText.text = "Ваш заказ оформлен и будет доставлен в ближайшее время!"
    }
}
