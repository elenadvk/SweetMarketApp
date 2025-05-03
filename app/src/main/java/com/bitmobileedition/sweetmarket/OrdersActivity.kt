package com.bitmobileedition.sweetmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.serialization.json.Json

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val recyclerView = findViewById<RecyclerView>(R.id.orders_list)
        val confirmButton = findViewById<Button>(R.id.confirm_orders_button)

        // Получаем данные из SharedPreferences
        val sp = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val cartItems = sp.getStringSet("cart", setOf())
            ?.filterNot { it.isNullOrBlank() }
            ?.toMutableList() ?: mutableListOf()

        // Получаем информацию о товарах из SharedPreferences
        val orders = mutableListOf<Item>()
        for (itemTitle in cartItems) {
            val itemJson = sp.getString("item_$itemTitle", null)
            val item = itemJson?.let { parseItemJson(it) }
            item?.let { orders.add(it) }
        }

        // Настройка RecyclerView и адаптера
        val adapter = OrdersAdapter(orders, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Обработка нажатия на кнопку "Оформить заказ"
        confirmButton.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            startActivity(intent)
//            Toast.makeText(this, "Заказ оформлен", Toast.LENGTH_SHORT).show()
        }
    }

    // Функция для парсинга JSON в объект Item
    private fun parseItemJson(json: String): Item? {
        return try {
            Json.decodeFromString<Item>(json)
        } catch (e: Exception) {
            null
        }
    }
}

