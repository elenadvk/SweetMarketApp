package com.bitmobileedition.sweetmarket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class ItemsAdapter(var items: List<Item>, var context: Context, val onClick: (String) -> Unit) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>(){


    //класс для поиска элементов товаров из дизайна
    /**
     * @param View
     * @property image the image of jewelery
     * @property title the name of jewelery
     * @property desc description of jewelery
     * @property price price of jewelery
     * @property btn button for watching information of jewelery
     * @constructor MyViewHolder creates holder with properties
     */
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val title: TextView = view.findViewById(R.id.item_list_title)
        val desc: TextView = view.findViewById(R.id.item_list_desc)
        val price: TextView = view.findViewById(R.id.item_list_price)

        val btn: Button = view.findViewById(R.id.item_list_button)
    }

    //с каким дизайном работаем
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    //обращаемся к найденным элементам и подставляем
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.desc.text = items[position].desc
        holder.price.text = items[position].price.toString() + "₽"

        //получение изображений из папки assets
        Glide.with(context).load("file:///android_asset/${items[position].image}").into(holder.image)

//        Glide.with(context).load("http://84.246.85.148:8080/${items[position].image}").into(holder.image)

        holder.btn.setOnClickListener{
            onClick(items[position].title)
            val intent = Intent(context, ItemActivity::class.java)

            intent.putExtra("itemTitle", items[position].title)
            intent.putExtra("itemText", items[position].text)
            //intent.putExtra("itemDesc", items[position].desc)
            intent.putExtra("itemImage", items[position].image)
            context.startActivity(intent)
        }
    }
}