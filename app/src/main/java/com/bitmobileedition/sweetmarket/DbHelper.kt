package com.bitmobileedition.sweetmarket

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "Sweet Market.db", factory, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE user_auth (id INTEGER PRIMARY KEY AUTOINCREMENT,user_login TEXT,user_email TEXT,user_password TEXT,user_status TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS user_auth")
        onCreate(db)
    }

    // Регистрация покупателя
    fun addUser(user: User) {
        val values = ContentValues().apply {
            put("user_login", user.buyer_login)
            put("user_email", user.buyer_email)
            put("user_password", user.buyer_password)
            put("user_status", user.buyer_status)
        }

        val db = this.writableDatabase
        db.insert("user_auth", null, values)
        db.close()
    }

    // Регистрация продавца
    fun addSeller(seller: Seller) {
        val values = ContentValues().apply {
            put("user_login", seller.seller_login)
            put("user_email", seller.seller_email)
            put("user_password", seller.seller_password)
            put("user_status", seller.seller_status)
        }

        val db = this.writableDatabase
        db.insert("user_auth", null, values)
        db.close()
    }

    // Авторизация покупателя
    fun getUser(buyer_login: String, buyer_password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM user_auth WHERE user_login = ? AND user_password = ? AND user_status = ?",
            arrayOf(buyer_login, buyer_password, "buyer")
        )

        val isAuthenticated = cursor.moveToFirst()
        cursor.close()
        return isAuthenticated
    }

    // Авторизация продавца
    fun getSeller(seller_login: String, seller_password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM user_auth WHERE user_login = ? AND user_password = ? AND user_status = ?",
            arrayOf(seller_login, seller_password, "seller")
        )

        val isAuthenticated = cursor.moveToFirst()
        cursor.close()
        return isAuthenticated
    }
}
