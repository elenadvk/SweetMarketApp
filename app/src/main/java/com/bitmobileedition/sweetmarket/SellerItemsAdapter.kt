package com.bitmobileedition.sweetmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SellerItemsAdapter(private val items: List<Item>) : RecyclerView.Adapter<SellerItemsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.item_title)
        val desc: TextView = view.findViewById(R.id.item_desc)
        val price: TextView = view.findViewById(R.id.item_price)
        val image: ImageView = view.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_in_seller_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.desc.text = item.text
        holder.price.text = "${item.price}₽"
        Glide.with(holder.image.context).load("file:///android_asset/${item.image}").into(holder.image)
    }
}
