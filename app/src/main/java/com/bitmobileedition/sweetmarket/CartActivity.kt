package com.bitmobileedition.sweetmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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
        val cartItems = sp.getStringSet("cart", setOf())?.toList() ?: emptyList()

        // Настройка адаптера для RecyclerView
        val adapter = CartAdapter(cartItems, this)
        cartListView.layoutManager = LinearLayoutManager(this)
        cartListView.adapter = adapter

        checkoutButton.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }
    }
}

