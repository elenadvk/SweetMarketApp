package com.bitmobileedition.sweetmarket

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.RecyclerView
import com.bitmobileedition.sweetmarket.ItemsViewModel

class ItemsActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

//        ///////////////////////////////////////////
        val goToCartButton: Button = findViewById(R.id.go_to_cart_button)
        goToCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

//        //////////////////////////

//        val viewModel: ItemsViewModel = ViewModelProvider(this).get()
        val viewModel: ItemsViewModel = ViewModelProvider(this)[ItemsViewModel::class.java]
        viewModel.loadItems()

        // Views
        val itemsList: RecyclerView = findViewById(R.id.itemslist)
        val placeholder = findViewById<TextView>(R.id.placeholder)
        val refreshButton = findViewById<Button>(R.id.refresh)
        val switch = findViewById<Switch>(R.id.theme_switch)
        val delete = findViewById<Button>(R.id.delete)
        // /Views


        val profileButton = findViewById<Button>(R.id.profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, SellerProfileActivity::class.java)
            startActivity(intent)
        }

        val sp = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        switch.isChecked = sp.getBoolean("theme", false)
        switch.setOnCheckedChangeListener { _, isChecked ->
            sp.edit { putBoolean("theme", isChecked) }
            if(isChecked) {
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
            } else {
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
            }
        }

        refreshButton.setOnClickListener { viewModel.filter() }
        val editText = findViewById<AutoCompleteTextView>(R.id.edit_text)
        editText.threshold = 1
        editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, sp.getStringSet("history", null)?.toList() ?: listOf()))
        editText.doOnTextChanged { text, _, _, _ ->
            viewModel.filter(text.toString())
        }
        delete.setOnClickListener {
            editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, listOf<String>()))
            sp.edit {
                putStringSet("history", setOf())
            }
        }
        val progressIndicator = findViewById<ProgressBar>(R.id.progress)

        viewModel.items.observe(this){
            progressIndicator.visibility = View.GONE
            itemsList.adapter = ItemsAdapter(it, this@ItemsActivity){
                val spSet = sp.getStringSet("history", null)
                val set = mutableSetOf<String>()
                spSet?.let { set.addAll(spSet) }
                set.add(it)
                sp.edit {
                    putStringSet("history", set)
                }
//                //////////////
                editText.setOnClickListener {
                    editText.showDropDown()
                }
//                /////////////
                editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, set.toList()))
            }
            if(it == null || it.isEmpty()){
                placeholder.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
            }else{
                refreshButton.visibility = View.GONE
                placeholder.visibility = View.GONE
            }
        }

//        /////////////////////////////////////////////////////////////////////////////
//        val cartButton = findViewById<Button>(R.id.cart_button)
//        cartButton.setOnClickListener {
//            val intent = Intent(this, CartActivity::class.java)
//            startActivity(intent)
//        }


    }
}