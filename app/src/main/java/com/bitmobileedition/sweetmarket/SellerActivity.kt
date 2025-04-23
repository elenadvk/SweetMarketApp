package com.bitmobileedition.sweetmarket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.json.Json

class SellerActivity : AppCompatActivity() {

    private lateinit var adapter: SellerItemsAdapter
    private lateinit var sharedPrefs: SharedPreferences
    private val items = mutableListOf<Item>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller)

        sharedPrefs = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        val inputTitle = findViewById<EditText>(R.id.input_title)
        val inputDesc = findViewById<EditText>(R.id.input_desc)
        val inputPrice = findViewById<EditText>(R.id.input_price)
        val inputImage = findViewById<EditText>(R.id.input_image)
        val addButton = findViewById<Button>(R.id.btn_add_item)
        val recyclerView = findViewById<RecyclerView>(R.id.seller_items_list)

        val to_items : Button = findViewById(R.id.to_items)
        to_items.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            startActivity(intent)
        }

        // Инициализируем адаптер
        adapter = SellerItemsAdapter(items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Загружаем уже сохраненные товары
        loadItems()

        // Обработчик нажатия кнопки для добавления товара
        addButton.setOnClickListener {
            val title = inputTitle.text.toString()
            val desc = inputDesc.text.toString()
            val priceString = inputPrice.text.toString()
            val image = inputImage.text.toString()

            if (title.isBlank() || desc.isBlank() || priceString.isBlank() || image.isBlank()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Преобразуем цену в Integer, обрабатываем ошибки
            val price = priceString.toIntOrNull() ?: run {
                Toast.makeText(this, "Неверный формат цены", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Генерируем id для нового товара
            val newItem = Item(
                id = (items.maxOfOrNull { it.id } ?: 0) + 1, // Генерация id
                title = title,
                desc = desc,
                text = desc, // Можно оставить desc как текст
                image = image,
                price = price
            )

            // Добавляем товар в начало списка
            items.add(0, newItem)
            adapter.notifyItemInserted(0)
            recyclerView.scrollToPosition(0)

            // Сохраняем обновленный список
            saveItems()

            // Очищаем поля ввода
            inputTitle.text.clear()
            inputDesc.text.clear()
            inputPrice.text.clear()
            inputImage.text.clear()
        }
    }

    private fun loadItems() {
        val jsonList = sharedPrefs.getString("items_list", null) ?: return
        try {
            val list = Json.decodeFromString<List<Item>>(jsonList)
            items.addAll(list)
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveItems() {
        val jsonList = Json.encodeToString(items)
        sharedPrefs.edit().putString("items_list", jsonList).apply()
    }

}
