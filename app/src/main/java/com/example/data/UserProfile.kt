package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val husbandName: String = "",
    val wifeName: String = "",
    val anniversaryDate: String = "", // e.g. "2022-11-25" (YYYY-MM-DD)
    val birthdayDate: String = "",    // e.g. "1998-04-14" (YYYY-MM-DD)
    val favoriteDrink: String = "",
    val favoriteFlower: String = "",
    val favoriteFood: String = "",
    val nickname: String = "",
    val phoneNumber: String = "",
    val ringSize: String = "",
    val shoeSize: String = "",
    val dressSize: String = "",
    val favoriteColor: String = ""
)
