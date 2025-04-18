package com.bitmobileedition.sweetmarket

import kotlinx.serialization.Serializable
@Serializable
data class Item(
    val id: Int,
    val image: String,
    val title: String,
    val desc: String,
    val text: String,
    val price: Int
)