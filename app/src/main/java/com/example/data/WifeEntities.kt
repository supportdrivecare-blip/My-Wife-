package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gift_wishes")
data class GiftWish(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String = "Surprise", // Jewelry, Clothes, Tech, Experience, Books, Accessories
    val notes: String = "",
    val priceEstimate: String = "",
    val occasion: String = "Anytime", // Birthday, Anniversary, Random
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
    val category: String = "Love", // Love, Appreciation, Compliment, Apology
    val isFavorite: Boolean = false,
    val isUserCreated: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "date_ideas")
data class DateIdea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val locationType: String = "Dining", // Dining, Outdoor, Home, Travel, Entertainment
    val isCompleted: Boolean = false,
    val isUserCreated: Boolean = false
)
