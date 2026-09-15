package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wife_profile")
data class WifeProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Priya",
    val nickname: String = "Jaanu / Meri Rani",
    val weddingYear: Int = 2022,
    val weddingMonth: Int = 11, // 1-12
    val weddingDay: Int = 25,
    val birthMonth: Int = 4,   // 1-12
    val birthDay: Int = 14,
    val phoneNumber: String = "",
    val chaiCoffee: String = "Masala Chai (Adrak, less sugar, boiled well)",
    val comfortFood: String = "Pani Puri & Butter Chicken with Garlic Naan",
    val favoriteDessert: String = "Warm Gulab Jamun with Vanilla Ice Cream",
    val ringSize: String = "US 6 / Indian 12",
    val shoeSize: String = "UK 5 / EU 38",
    val dressSize: String = "Medium / Kurti 38",
    val favoriteColor: String = "Rose Gold & Pastel Pink",
    val favoriteFlowers: String = "Red Roses & Fresh White Lilies",
    val favoritePerfume: String = "Floral & Vanilla notes",
    val moodFixer: String = "Head massage, chocolates & sweet hugs without asking",
    val songOrMovie: String = "Tum Hi Ho / Jab We Met"
)

@Entity(tableName = "gift_wishes")
data class GiftWish(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String = "Surprise", // Jewelry, Clothes, Tech, Experience, Books, Accessories
    val notes: String = "",
    val priceEstimate: String = "",
    val occasion: String = "Anytime", // Birthday, Anniversary, Karwa Chauth, Random
    val isFulfilled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_care")
data class DailyCareItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateKey: String, // e.g. "2026-09-11"
    val taskKey: String,
    val title: String,
    val subtitle: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "love_notes")
data class LoveNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val hindiText: String = "",
    val category: String = "Love", // Love, Appreciation, Compliment, Apology, Memory
    val isFavorite: Boolean = false,
    val isUserCreated: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "date_ideas")
data class DateIdea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val locationType: String = "Outdoor", // Home, Outdoor, Dining, Weekend
    val isCompleted: Boolean = false,
    val isUserCreated: Boolean = false
)
