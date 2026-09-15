package com.example.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WifeRepository(private val dao: WifeDao) {

    val profile: Flow<WifeProfile?> = dao.getProfile()
    val gifts: Flow<List<GiftWish>> = dao.getAllGifts()
    val loveNotes: Flow<List<LoveNote>> = dao.getAllLoveNotes()
    val dateIdeas: Flow<List<DateIdea>> = dao.getAllDateIdeas()

    fun getCareItemsForToday(): Flow<List<DailyCareItem>> {
        val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return dao.getCareItemsForDate(todayStr)
    }

    suspend fun saveProfile(profile: WifeProfile) {
        dao.saveProfile(profile)
    }

    suspend fun ensureInitialized() {
        // Initialize default profile if absent
        dao.saveProfile(
            WifeProfile(
                id = 1,
                name = "Priya",
                nickname = "Jaanu / Meri Begum",
                weddingYear = 2022,
                weddingMonth = 11,
                weddingDay = 25,
                birthMonth = 4,
                birthDay = 14,
                phoneNumber = "",
                chaiCoffee = "Masala Chai (Adrak, less sugar, boiled well)",
                comfortFood = "Pani Puri & Butter Chicken with Garlic Naan",
                favoriteDessert = "Warm Gulab Jamun with Vanilla Ice Cream",
                ringSize = "US 6 / Indian 12",
                shoeSize = "UK 5 / EU 38",
                dressSize = "Medium / Kurti 38",
                favoriteColor = "Pastel Pink & Rose Gold",
                favoriteFlowers = "Red Roses & Fresh White Lilies",
                favoritePerfume = "Floral & Vanilla notes",
                moodFixer = "Gentle hug, sweet treats & listening without judgment",
                songOrMovie = "Tum Hi Ho / Jab We Met"
            )
        )

        // Seed initial love notes
        val initialNotes = listOf(
            LoveNote(
                id = 1,
                title = "Meri Zindagi Ki Roshni",
                hindiText = "Har subah tumhari muskaan dekh kar lagta hai ki maine zindagi mein kuch toh bahut achha kiya tha.",
                message = "Seeing your smile each morning makes me believe I did something truly wonderful in life. You make every day brighter.",
                category = "Love",
                isFavorite = true,
                isUserCreated = false
            ),
            LoveNote(
                id = 2,
                title = "Chai & Sukoon",
                hindiText = "Duniya ki saari bheed ek taraf, aur shaam ko tumhare saath baith kar chai peena ek taraf.",
                message = "The entire world's noise on one side, and sitting down to enjoy evening chai with you on the other. That is true peace.",
                category = "Appreciation",
                isFavorite = true,
                isUserCreated = false
            ),
            LoveNote(
                id = 3,
                title = "Always Radiant",
                hindiText = "Tum jab dil khol kar hasti ho, duniya ki sabse khoobsurat lagti ho.",
                message = "You look the most radiant when you laugh with your whole heart. Never forget how deeply loved and treasured you are.",
                category = "Compliment",
                isFavorite = false,
                isUserCreated = false
            ),
            LoveNote(
                id = 4,
                title = "My Anchor & Best Friend",
                hindiText = "Har mod par mera saath dene ke liye shukriya. Tum sirf meri patni nahi, meri sabse achhi dost ho.",
                message = "Thank you for walking beside me through every high and low. You are not only my wife, but my closest confidante and best friend.",
                category = "Appreciation",
                isFavorite = false,
                isUserCreated = false
            ),
            LoveNote(
                id = 5,
                title = "Dil Se Maafi (Apology Note)",
                hindiText = "Agar meri kisi baat ya galti se tumhara dil dukha ho, toh mujhe dil se maaf kar dena. Tumhari khushi mere liye sabse zaroori hai.",
                message = "If my words or actions ever caused you pain, please forgive me from your heart. Your happiness and peace matter more to me than anything.",
                category = "Apology",
                isFavorite = false,
                isUserCreated = false
            )
        )
        dao.insertInitialNotes(initialNotes)

        // Seed initial date ideas
        val initialIdeas = listOf(
            DateIdea(
                id = 1,
                title = "Sunset Chai & Long Drive",
                description = "Pick her up unexpectedly, play her favorite songs, and grab hot tea at a scenic viewpoint.",
                locationType = "Outdoor",
                isCompleted = false
            ),
            DateIdea(
                id = 2,
                title = "Candlelight Dinner at Home",
                description = "Cook her comfort food, dim the ambient lights, light scented candles, and play soft acoustics.",
                locationType = "Home",
                isCompleted = false
            ),
            DateIdea(
                id = 3,
                title = "Late Night Ice Cream Run",
                description = "Sneak out past midnight in comfy pajamas for dessert scoops and a quiet walk.",
                locationType = "Outdoor",
                isCompleted = false
            ),
            DateIdea(
                id = 4,
                title = "Pampering & Relaxation Night",
                description = "Warm foot soak, head massage, a soothing playlist, and her favorite chocolates after a busy week.",
                locationType = "Home",
                isCompleted = false
            ),
            DateIdea(
                id = 5,
                title = "Weekend Heritage Cafe Visit",
                description = "Dress up nicely, take plenty of photos of her, and try a charming new cafe she bookmarked.",
                locationType = "Dining",
                isCompleted = false
            )
        )
        dao.insertInitialDateIdeas(initialIdeas)
    }

    suspend fun ensureTodayCareChecklist() {
        val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val defaultCareTasks = listOf(
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "compliment",
                title = "Give a genuine compliment",
                subtitle = "Tell her how lovely she looks or appreciate something she did today",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "checkin",
                title = "Ask 'How was your day?'",
                subtitle = "Listen actively for 5 minutes without giving unsolicited advice",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "beverage",
                title = "Offer her favorite beverage",
                subtitle = "Brew her exact chai or coffee preferences unprompted",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "hug",
                title = "Give a warm 20-second hug",
                subtitle = "A long, reassuring embrace to release stress hormones",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "chores",
                title = "Take care of a chore unasked",
                subtitle = "Wash dishes, fold clothes, or tidy the room quietly",
                isCompleted = false
            )
        )
        dao.insertCareItems(defaultCareTasks)
    }

    suspend fun toggleCareItem(id: Int, completed: Boolean) {
        dao.setCareItemCompleted(id, completed)
    }

    // Gifts
    suspend fun addGift(gift: GiftWish) = dao.insertGift(gift)
    suspend fun updateGift(gift: GiftWish) = dao.updateGift(gift)
    suspend fun deleteGift(gift: GiftWish) = dao.deleteGift(gift)

    // Love Notes
    suspend fun addLoveNote(note: LoveNote) = dao.insertLoveNote(note)
    suspend fun updateLoveNote(note: LoveNote) = dao.updateLoveNote(note)
    suspend fun deleteLoveNote(note: LoveNote) = dao.deleteLoveNote(note)

    // Date Ideas
    suspend fun addDateIdea(idea: DateIdea) = dao.insertDateIdea(idea)
    suspend fun updateDateIdea(idea: DateIdea) = dao.updateDateIdea(idea)
    suspend fun deleteDateIdea(idea: DateIdea) = dao.deleteDateIdea(idea)
}
