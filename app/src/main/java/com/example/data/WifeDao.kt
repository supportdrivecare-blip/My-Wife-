package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WifeDao {
    // Profile
    @Query("SELECT * FROM wife_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<WifeProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: WifeProfile)

    // Gift Wishes
    @Query("SELECT * FROM gift_wishes ORDER BY isFulfilled ASC, createdAt DESC")
    fun getAllGifts(): Flow<List<GiftWish>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGift(gift: GiftWish): Long

    @Update
    suspend fun updateGift(gift: GiftWish)

    @Delete
    suspend fun deleteGift(gift: GiftWish)

    // Daily Care Items
    @Query("SELECT * FROM daily_care WHERE dateKey = :dateKey")
    fun getCareItemsForDate(dateKey: String): Flow<List<DailyCareItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareItems(items: List<DailyCareItem>)

    @Query("UPDATE daily_care SET isCompleted = :completed WHERE id = :id")
    suspend fun setCareItemCompleted(id: Int, completed: Boolean)

    // Love Notes
    @Query("SELECT * FROM love_notes ORDER BY isFavorite DESC, createdAt DESC")
    fun getAllLoveNotes(): Flow<List<LoveNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoveNote(note: LoveNote): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialNotes(notes: List<LoveNote>)

    @Update
    suspend fun updateLoveNote(note: LoveNote)

    @Delete
    suspend fun deleteLoveNote(note: LoveNote)

    // Date Ideas
    @Query("SELECT * FROM date_ideas ORDER BY isCompleted ASC, id ASC")
    fun getAllDateIdeas(): Flow<List<DateIdea>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialDateIdeas(ideas: List<DateIdea>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDateIdea(idea: DateIdea): Long

    @Update
    suspend fun updateDateIdea(idea: DateIdea)

    @Delete
    suspend fun deleteDateIdea(idea: DateIdea)
}
