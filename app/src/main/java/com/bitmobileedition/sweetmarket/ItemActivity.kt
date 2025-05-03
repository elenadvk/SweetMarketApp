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

        // Инициализация элементов интерфейса
        val title: TextView = findViewById(R.id.item_list_title1)
        val text: TextView = findViewById(R.id.item_list_text)
        val imageView: ImageView = findViewById(R.id.item_list_image)
        val description: TextView = findViewById(R.id.item_list_desc)
        val price: TextView = findViewById(R.id.item_list_price)
        val buyButton: Button = findViewById(R.id.button_buy)

        // Получаем все данные о товаре через Intent
        val itemTitle = intent.getStringExtra("itemTitle") ?: ""
        val itemText = intent.getStringExtra("itemText") ?: ""
        val itemImage = intent.getStringExtra("itemImage") ?: ""
        val itemDesc = intent.getStringExtra("itemDesc") ?: ""
        val itemPrice = intent.getIntExtra("itemPrice", 0)

        // Отображаем данные на экране
        title.text = itemTitle
        text.text = itemText
        description.text = itemDesc
        price.text = "${itemPrice}₽"
        Glide.with(this).load("file:///android_asset/${itemImage}").into(imageView)

        // Обработчик кнопки "Добавить в корзину"
        buyButton.setOnClickListener {
            val cartItems = sp.getStringSet("cart", setOf())?.toMutableSet() ?: mutableSetOf()
            val item = Item(
                id = 0,
                title = itemTitle,
                text = itemText,
                image = itemImage,
                desc = itemDesc,
                price = itemPrice
            )

            val itemJson = Json.encodeToString(Item.serializer(), item)
            sp.edit().putString("item_$itemTitle", itemJson).apply()

            cartItems.add(itemTitle)
            sp.edit().putStringSet("cart", cartItems).apply()

            Toast.makeText(this, "$itemTitle добавлен в корзину!", Toast.LENGTH_SHORT).show()
        }
    }
}


//class ItemActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_item)
//
//        val sp = getSharedPreferences("preferences", Context.MODE_PRIVATE)
//
//        // Инициализация элементов интерфейса
//        val title: TextView = findViewById(R.id.item_list_title1)
//        val text: TextView = findViewById(R.id.item_list_text)
//        val imageView: ImageView = findViewById(R.id.item_list_image)
//        val description: TextView = findViewById(R.id.item_list_desc) // Для описания
//        val price: TextView = findViewById(R.id.item_list_price) // Для цены
//        val buyButton: Button = findViewById(R.id.button_buy)
//
//        // Получаем все данные о товаре через Intent
//        val itemTitle = intent.getStringExtra("itemTitle") ?: ""
//        val itemText = intent.getStringExtra("itemText") ?: ""
//        val itemImage = intent.getStringExtra("itemImage") ?: ""
//        val itemDesc = intent.getStringExtra("itemDesc") ?: ""
//        val itemPrice = intent.getIntExtra("itemPrice", 0)
//
//        // Отображаем данные на экране
//        title.text = itemTitle
//        text.text = itemText
//        description.text = itemDesc
//        price.text = "${itemPrice}₽"
//        Glide.with(this).load("file:///android_asset/${itemImage}").into(imageView)
//
//        // Обработчик кнопки "Добавить в корзину"
//        buyButton.setOnClickListener {
//            val cartItems = sp.getStringSet("cart", setOf())?.toMutableSet() ?: mutableSetOf()
//            val item = Item(
//                id = 0,
//                title = itemTitle,
//                text = itemText,
//                image = itemImage,
//                desc = itemDesc,
//                price = itemPrice
//            )
//
//            // Сохраняем товар в корзину
//            val itemJson = Json.encodeToString(Item.serializer(), item)
//            sp.edit().putString("item_$itemTitle", itemJson).apply()
//
//            // Добавляем товар в корзину
//            cartItems.add(itemTitle)
//            sp.edit().putStringSet("cart", cartItems).apply()
//
//            Toast.makeText(this, "$itemTitle добавлен в корзину!", Toast.LENGTH_SHORT).show()
//        }
//    }
//}