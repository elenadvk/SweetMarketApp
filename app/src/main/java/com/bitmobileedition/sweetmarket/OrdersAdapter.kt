package com.bitmobileedition.sweetmarket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.Toast

class OrdersAdapter(private val orders: MutableList<Item>, private val context: Context) :
    RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val title: TextView = view.findViewById(R.id.item_list_title)
        val price: TextView = view.findViewById(R.id.item_list_price)
        val email: TextView = view.findViewById(R.id.item_list_email)
        val statusText: TextView = view.findViewById(R.id.status_text)
        val cancelOrderButton: Button = view.findViewById(R.id.cancel_order_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_in_order_list, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.title.text = orders[position].title
        holder.price.text = "${orders[position].price}₽"
        holder.email.text = "Email продавца: elenadubikova17@gmail.com"
        holder.statusText.text = "Статус: -"

        // Загружаем изображение товара
        Glide.with(context).load("file:///android_asset/${orders[position].image}").into(holder.image)

        // При нажатии на товар меняем статус на "В работе"
        holder.itemView.setOnClickListener {
            holder.statusText.text = "Статус: В работе"
        }

        // При нажатии на кнопку "Отменить заказ" удаляем товар
        holder.cancelOrderButton.setOnClickListener {
            orders.removeAt(position)
            notifyItemRemoved(position)
            Toast.makeText(context, "${orders[position].title} отменён", Toast.LENGTH_SHORT).show()
        }
    }

    private fun item(position: Int) = orders[position]
}
