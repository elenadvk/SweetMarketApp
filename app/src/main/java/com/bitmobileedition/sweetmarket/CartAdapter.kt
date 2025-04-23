package com.bitmobileedition.sweetmarket

import android.content.Context
import android.content.SharedPreferences
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.serialization.json.Json

class CartAdapter(private var cartItems: MutableList<String>, private val context: Context) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cart_item_image)
        val title: TextView = view.findViewById(R.id.cart_item_title)
        val price: TextView = view.findViewById(R.id.cart_item_price)
//        val viewDetailsBtn: Button = view.findViewById(R.id.view_details_button)
        val deleteBtn: Button = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val itemTitle = cartItems[position]
        val sp = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

        val itemJson = sp.getString("item_$itemTitle", null)
        val item = itemJson?.let { parseItemJson(it) }

        if (item == null) {
            // Удаляем мусор из корзины
            cartItems.removeAt(position)
            sp.edit().putStringSet("cart", cartItems.toSet()).apply()
            notifyItemRemoved(position)
            return
        }

        holder.title.text = item.title
        holder.price.text = "${item.price}₽"
        Glide.with(context).load("file:///android_asset/${item.image}").into(holder.image)

        holder.image.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java).apply {
                putExtra("itemTitle", item.title)
                putExtra("itemText", item.text)
                putExtra("itemImage", item.image)
            }
            context.startActivity(intent)
        }

//        holder.viewDetailsBtn.setOnClickListener {
//            val intent = Intent(context, ItemActivity::class.java).apply {
//                putExtra("itemTitle", item.title)
//                putExtra("itemText", item.text)
//                putExtra("itemImage", item.image)
//            }
//            context.startActivity(intent)
//        }

        holder.deleteBtn.setOnClickListener {
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)

            val updatedSet = cartItems.toSet()
            sp.edit().putStringSet("cart", updatedSet).apply()
            sp.edit().remove("item_${item.title}").apply()

            Toast.makeText(context, "${item.title} удалён из корзины", Toast.LENGTH_SHORT).show()
        }
    }


    private fun parseItemJson(json: String): Item? {
        return try {
            Json.decodeFromString<Item>(json)
        } catch (e: Exception) {
            null
        }
    }

}


//class CartAdapter(private val cartItems: List<String>, private val context: Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    // ViewHolder для отображения элементов корзины
//    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val image: ImageView = view.findViewById(R.id.cart_item_image)
//        val title: TextView = view.findViewById(R.id.cart_item_title)
//        val price: TextView = view.findViewById(R.id.cart_item_price)
//        val viewDetailsBtn: Button = view.findViewById(R.id.view_details_button)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)
//        return CartViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = cartItems.size
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        val itemTitle = cartItems[position]
//        val sp = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
//
//        // Получаем товар из списка, если есть
//        val itemJson = sp.getString("item_$itemTitle", null)
//        val item = itemJson?.let { parseItemJson(it) }
//
//        // Если товар найден, обновляем данные
//        item?.let {
//            holder.title.text = it.title
//            holder.price.text = "${it.price}₽"
//            Glide.with(context).load("file:///android_asset/${it.image}").into(holder.image)
//
//            holder.viewDetailsBtn.setOnClickListener {
//                item?.let { item ->
//                    // Открываем экран с подробной информацией о товаре
//                    val intent = Intent(context, ItemActivity::class.java).apply {
//                        putExtra("itemTitle", item.title)
//                        putExtra("itemText", item.text)
//                        putExtra("itemImage", item.image)
//                    }
//                    context.startActivity(intent)
//                }
//            }
//
//        }
//    }
//
//    // Функция для парсинга объекта Item из JSON
//    private fun parseItemJson(json: String): Item? {
//        return try {
//            Json.decodeFromString<Item>(json)
//        } catch (e: Exception) {
//            null
//        }
//    }
//}
