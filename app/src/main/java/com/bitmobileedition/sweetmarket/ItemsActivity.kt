package com.bitmobileedition.sweetmarket

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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

        // Views
        val rootLayout = findViewById<ConstraintLayout>(R.id.root_layout)
        val goToCartButton: Button = findViewById(R.id.go_to_cart_button)
        val itemsList: RecyclerView = findViewById(R.id.itemslist)
        val placeholder = findViewById<TextView>(R.id.placeholder)
        val refreshButton = findViewById<Button>(R.id.refresh)
        val switch = findViewById<Switch>(R.id.theme_switch)
        val delete = findViewById<Button>(R.id.delete)
        val profileButton = findViewById<Button>(R.id.profile)

        // ViewModel
        val viewModel: ItemsViewModel = ViewModelProvider(this).get()

        // SharedPreferences
        val sp = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        // Initialize the UI mode manager
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

        // Set initial theme based on SharedPreferences
        val isDarkMode = sp.getBoolean("theme", false)
        switch.isChecked = isDarkMode
        setAppTheme(isDarkMode)

        // Handle theme switch change
        switch.setOnCheckedChangeListener { _, isChecked ->
            sp.edit { putBoolean("theme", isChecked) }
            uiModeManager.setApplicationNightMode(
                if (isChecked) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
            )
            setAppTheme(isChecked)
        }

        // Set onClick listener for go to cart button
        goToCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Set onClick listener for profile button
        profileButton.setOnClickListener {
            val intent = Intent(this, SellerProfileActivity::class.java)
            startActivity(intent)
        }

        // Search input setup
        val editText = findViewById<AutoCompleteTextView>(R.id.edit_text)
        editText.threshold = 1
        editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, sp.getStringSet("history", null)?.toList() ?: listOf()))
        editText.doOnTextChanged { text, _, _, _ -> viewModel.filter(text.toString()) }

        // Clear search history on delete button click
        delete.setOnClickListener {
            editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, listOf<String>()))
            sp.edit { putStringSet("history", setOf()) }
        }

        // Progress indicator setup
        val progressIndicator = findViewById<ProgressBar>(R.id.progress)

        // Observe changes to items and update RecyclerView
        viewModel.items.observe(this) { items ->
            progressIndicator.visibility = View.GONE
            itemsList.adapter = ItemsAdapter(items, this@ItemsActivity) {
                val spSet = sp.getStringSet("history", null)
                val set = mutableSetOf<String>()
                spSet?.let { set.addAll(spSet) }
                set.add(it)
                sp.edit { putStringSet("history", set) }

                // Show the dropdown when editing text
                editText.setOnClickListener { editText.showDropDown() }
                editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, set.toList()))
            }
            if (items.isNullOrEmpty()) {
                placeholder.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
            } else {
                placeholder.visibility = View.GONE
                refreshButton.visibility = View.GONE
            }
        }

        // Refresh items on refresh button click
        refreshButton.setOnClickListener { viewModel.filter() }
    }

    // Helper method to set the theme
    private fun setAppTheme(isDarkMode: Boolean) {
        val rootLayout = findViewById<ConstraintLayout>(R.id.root_layout)

        if (isDarkMode) {
            rootLayout.setBackgroundResource(R.color.brown) // Dark mode background color
        } else {
            rootLayout.setBackgroundResource(R.color.beije) // Light mode background color
        }
    }
}



//package com.bitmobileedition.sweetmarket
//
//import android.annotation.SuppressLint
//import android.app.UiModeManager
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.ArrayAdapter
//import android.widget.AutoCompleteTextView
//import android.widget.Button
//import android.widget.ProgressBar
//import android.widget.Switch
//import android.widget.TextView
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.edit
//import androidx.core.widget.doOnTextChanged
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.get
//import androidx.recyclerview.widget.RecyclerView
//import com.bitmobileedition.sweetmarket.ItemsViewModel
//
//class ItemsActivity : AppCompatActivity() {
//
//
//    @RequiresApi(Build.VERSION_CODES.S)
//    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_items)
//
////        ///////////////////////////////////////////
//        val goToCartButton: Button = findViewById(R.id.go_to_cart_button)
//        goToCartButton.setOnClickListener {
//            val intent = Intent(this, CartActivity::class.java)
//            startActivity(intent)
//        }
//
////        //////////////////////////
//
//        val viewModel: ItemsViewModel = ViewModelProvider(this).get()
//
//        // Views
//        val itemsList: RecyclerView = findViewById(R.id.itemslist)
//        val placeholder = findViewById<TextView>(R.id.placeholder)
//        val refreshButton = findViewById<Button>(R.id.refresh)
//        val switch = findViewById<Switch>(R.id.theme_switch)
//        val delete = findViewById<Button>(R.id.delete)
//        // /Views
//
//
//        val profileButton = findViewById<Button>(R.id.profile)
//        profileButton.setOnClickListener {
//            val intent = Intent(this, SellerProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        val sp = getSharedPreferences("preferences", Context.MODE_PRIVATE)
//
//        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//        switch.isChecked = sp.getBoolean("theme", false)
//        switch.setOnCheckedChangeListener { _, isChecked ->
//            sp.edit { putBoolean("theme", isChecked) }
//            if(isChecked) {
//                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
//            } else {
//                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
//            }
//        }
//
//        refreshButton.setOnClickListener { viewModel.filter() }
//        val editText = findViewById<AutoCompleteTextView>(R.id.edit_text)
//        editText.threshold = 1
//        editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, sp.getStringSet("history", null)?.toList() ?: listOf()))
//        editText.doOnTextChanged { text, _, _, _ ->
//            viewModel.filter(text.toString())
//        }
//        delete.setOnClickListener {
//            editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, listOf<String>()))
//            sp.edit {
//                putStringSet("history", setOf())
//            }
//        }
//        val progressIndicator = findViewById<ProgressBar>(R.id.progress)
//
//        viewModel.items.observe(this){
//            progressIndicator.visibility = View.GONE
//            itemsList.adapter = ItemsAdapter(it, this@ItemsActivity){
//                val spSet = sp.getStringSet("history", null)
//                val set = mutableSetOf<String>()
//                spSet?.let { set.addAll(spSet) }
//                set.add(it)
//                sp.edit {
//                    putStringSet("history", set)
//                }
////                //////////////
//                editText.setOnClickListener {
//                    editText.showDropDown()
//                }
////                /////////////
//                editText.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, set.toList()))
//            }
//            if(it == null || it.isEmpty()){
//                placeholder.visibility = View.VISIBLE
//                refreshButton.visibility = View.VISIBLE
//            }else{
//                refreshButton.visibility = View.GONE
//                placeholder.visibility = View.GONE
//            }
//        }
//
////        /////////////////////////////////////////////////////////////////////////////
////        val cartButton = findViewById<Button>(R.id.cart_button)
////        cartButton.setOnClickListener {
////            val intent = Intent(this, CartActivity::class.java)
////            startActivity(intent)
////        }
//
//
//    }
//}