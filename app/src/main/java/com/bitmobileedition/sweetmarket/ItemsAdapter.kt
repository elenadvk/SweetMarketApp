package com.bitmobileedition.sweetmarket

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class ItemsAdapter(var items: List<Item>, var context: Context, val onClick: (String) -> Unit) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val title: TextView = view.findViewById(R.id.item_list_title)
        val desc: TextView = view.findViewById(R.id.item_list_desc)
        val price: TextView = view.findViewById(R.id.item_list_price)
//        val btn: Button = view.findViewById(R.id.item_list_button)
        val addToCartBtn: Button = view.findViewById(R.id.add_to_cart_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.desc.text = items[position].desc
        holder.price.text = "${items[position].price}₽"

        Glide.with(context).load("file:///android_asset/${items[position].image}").into(holder.image)

        holder.addToCartBtn.setOnClickListener {
            val sp = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val cartItems = sp.getStringSet("cart", setOf())?.toMutableSet() ?: mutableSetOf()

            val itemJson = Json.encodeToString(items[position])
            sp.edit().putString("item_${items[position].title}", itemJson).apply()

            cartItems.add(items[position].title)
            sp.edit().putStringSet("cart", cartItems).apply()

            Toast.makeText(context, "${items[position].title} добавлен в корзину!", Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("itemTitle", items[position].title)
            intent.putExtra("itemText", items[position].text)
            intent.putExtra("itemImage", items[position].image)
            intent.putExtra("itemDesc", items[position].desc) // Добавляем описание
            intent.putExtra("itemPrice", items[position].price) // Добавляем цену
            context.startActivity(intent)
        }
//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, ItemActivity::class.java)
//            intent.putExtra("itemTitle", items[position].title)
//            intent.putExtra("itemText", items[position].text)
//            intent.putExtra("itemImage", items[position].image)
//            intent.putExtra("itemPrice", items[position].price)
//            context.startActivity(intent)
//        }
//        holder.btn.setOnClickListener {
//            onClick(items[position].title)
//            val intent = Intent(context, ItemActivity::class.java)
//            intent.putExtra("itemTitle", items[position].title)
//            intent.putExtra("itemText", items[position].text)
//            intent.putExtra("itemImage", items[position].image)
//            context.startActivity(intent)
//        }
    }
}
