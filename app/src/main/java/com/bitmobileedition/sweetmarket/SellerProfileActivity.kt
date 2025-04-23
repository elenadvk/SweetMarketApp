package com.bitmobileedition.sweetmarket

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit


class SellerProfileActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var editButton: Button
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        editButton = findViewById(R.id.edit_button)

        val sp = getSharedPreferences("user", Context.MODE_PRIVATE)
        username.setText(sp.getString("username", "Пользователь"))
        email.setText(sp.getString("email", "email@example.com"))

        editButton.setOnClickListener {
            isEditing = !isEditing
            username.isEnabled = isEditing
            email.isEnabled = isEditing
            editButton.text = if (isEditing) "Сохранить" else "Редактировать"

            if (!isEditing) {
                sp.edit {
                    putString("username", username.text.toString())
                    putString("email", email.text.toString())
                }
            }
        }
    }
}