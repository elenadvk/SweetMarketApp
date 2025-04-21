package com.bitmobileedition.sweetmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartListView: RecyclerView = findViewById(R.id.cart_list)
        val checkoutButton: Button = findViewById(R.id.checkoutButton)

        val sp = getSharedPreferences("preferences", Context.MODE_PRIVATE)
//        val cartItems = sp.getStringSet("cart", setOf())?.toList() ?: emptyList()
//        val cartItems = sp.getStringSet("cart", setOf())?.toMutableList() ?: mutableListOf()
        val cartItems = sp.getStringSet("cart", setOf())
            ?.filterNot { it.isNullOrBlank() }
            ?.toMutableList() ?: mutableListOf()



        // Настройка адаптера для RecyclerView
//        val adapter = CartAdapter(cartItems, this)
        val adapter = CartAdapter(cartItems, this)
        cartListView.layoutManager = LinearLayoutManager(this)
        cartListView.adapter = adapter

        checkoutButton.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }

        val clearCartButton: Button = findViewById(R.id.clear_cart_button)

        clearCartButton.setOnClickListener {
            val editor = sp.edit()
            val cartItems = sp.getStringSet("cart", setOf()) ?: setOf()

            // Удаляем сохранённые данные о каждом товаре
            for (title in cartItems) {
                editor.remove("item_$title")
            }

            // Очищаем саму корзину
            editor.remove("cart")
            editor.apply()

            // Обновляем адаптер
            val adapter = CartAdapter(mutableListOf(), this)
            cartListView.adapter = adapter

            Toast.makeText(this, "Корзина очищена", Toast.LENGTH_SHORT).show()

        }

        checkoutButton.setOnClickListener {
            finish()
        }


    }
}

