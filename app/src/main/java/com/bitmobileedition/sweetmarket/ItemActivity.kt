package com.bitmobileedition.sweetmarket

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.serialization.json.Json

class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val sp = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val title: TextView = findViewById(R.id.item_list_title1)
        val text: TextView = findViewById(R.id.item_list_text)
        val imageView: ImageView = findViewById(R.id.item_list_image)
        val buyButton: Button = findViewById(R.id.button_buy)

        val itemTitle = intent.getStringExtra("itemTitle") ?: ""
        val itemText = intent.getStringExtra("itemText") ?: ""
        val itemImage = intent.getStringExtra("itemImage") ?: ""

        title.text = intent.getStringExtra("itemTitle")
        Glide.with(this).load("file:///android_asset/${intent.getStringExtra("itemImage")}").into(imageView)
//        Glide.with(this).load("http://84.246.85.148:8080/${intent.getStringExtra("itemImage")}").into(imageView)
        text.text = intent.getStringExtra("itemText")

        buyButton.setOnClickListener {
            val cartItems = sp.getStringSet("cart", setOf())?.toMutableSet() ?: mutableSetOf()
            val item = Item(
                id = 0, // или другое значение по умолчанию
                title = itemTitle,
                text = itemText,
                image = itemImage,
                desc = "",
                price = 0
            )

            val itemJson = Json.encodeToString(item)

            sp.edit().putString("item_$itemTitle", itemJson).apply()
            cartItems.add(itemTitle)
            sp.edit().putStringSet("cart", cartItems).apply()

            Toast.makeText(this, "$itemTitle добавлен в корзину!", Toast.LENGTH_SHORT).show()
        }
    }
}