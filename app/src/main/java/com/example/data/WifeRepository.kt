package com.example.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WifeRepository(
    private val profileDao: UserProfileDao,
    private val giftDao: GiftWishDao,
    private val careDao: DailyCareItemDao,
    private val loveNoteDao: LoveNoteDao,
    private val dateIdeaDao: DateIdeaDao
) {

    val profile: Flow<UserProfile?> = profileDao.getProfile()
    val gifts: Flow<List<GiftWish>> = giftDao.getAllGifts()
    val loveNotes: Flow<List<LoveNote>> = loveNoteDao.getAllLoveNotes()
    val dateIdeas: Flow<List<DateIdea>> = dateIdeaDao.getAllDateIdeas()

    fun getCareItemsForToday(): Flow<List<DailyCareItem>> {
        val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return careDao.getCareItemsForDate(todayStr)
    }

    suspend fun saveProfile(profile: UserProfile) {
        profileDao.insertProfile(profile)
    }

    suspend fun updateProfile(profile: UserProfile) {
        profileDao.updateProfile(profile)
    }

    suspend fun deleteProfile() {
        profileDao.deleteProfile()
    }

    suspend fun ensureInitialContent() {
        // Seed universal romantic love notes (without any hardcoded user names)
        val initialNotes = listOf(
            LoveNote(
                id = 1,
                title = "Meri Zindagi Ki Roshni",
                hindiText = "Har subah tumhari muskaan dekh kar lagta hai ke maine zindagi mein koi bahut pyara kaam kiya tha.",
                message = "Seeing your smile each day makes me feel truly blessed. You bring warmth, grace, and light into our life together.",
                category = "Love",
                isFavorite = true,
                isUserCreated = false
            ),
            LoveNote(
                id = 2,
                title = "Sukoon Aur Pyar",
                hindiText = "Duniya ki saari bheed ek taraf, aur shaam ko tumhare saath baith kar chai peena ek taraf.",
                message = "The entire noisy world on one side, and sitting peacefully beside you on the other. That is true happiness.",
                category = "Appreciation",
                isFavorite = true,
                isUserCreated = false
            ),
            LoveNote(
                id = 3,
                title = "Always Radiant",
                hindiText = "Tum jab dil khol kar hasti ho, duniya ki sabse khoobsurat lagti ho.",
                message = "You look the most enchanting when you smile and laugh freely. Always remember how deeply treasured you are.",
                category = "Compliment",
                isFavorite = false,
                isUserCreated = false
            ),
            LoveNote(
                id = 4,
                title = "My Anchor & Best Friend",
                hindiText = "Har mod par mera saath dene ke liye shukriya. Tum sirf meri biwi nahi, meri sabse achhi dost ho.",
                message = "Thank you for standing by my side through every journey. You are not only my wife, but my dearest companion.",
                category = "Appreciation",
                isFavorite = false,
                isUserCreated = false
            ),
            LoveNote(
                id = 5,
                title = "Dil Se Maafi (Apology Note)",
                hindiText = "Agar meri kisi baat ya bhool se tumhara dil dukha ho, toh mujhe dil se maaf kar dena. Tumhari khushi sabse ahem hai.",
                message = "If my words or unintended mistakes ever hurt you, please forgive me from the heart. Your peace and happiness mean everything.",
                category = "Apology",
                isFavorite = false,
                isUserCreated = false
            )
        )
        loveNoteDao.insertInitialNotes(initialNotes)

        // Seed inspiring date ideas
        val initialIdeas = listOf(
            DateIdea(
                id = 1,
                title = "Sunset Drive & Quiet Tea",
                description = "Pick her up unexpectedly, play relaxing songs she loves, and enjoy hot drinks at a scenic spot.",
                locationType = "Outdoor",
                isCompleted = false
            ),
            DateIdea(
                id = 2,
                title = "Candlelight Dinner at Home",
                description = "Cook her favorite meal, dim the ambient lights, light soft candles, and enjoy quality conversation.",
                locationType = "Home",
                isCompleted = false
            ),
            DateIdea(
                id = 3,
                title = "Late Night Sweet Treat Run",
                description = "Head out in cozy clothes after hours for dessert, ice cream, or waffles.",
                locationType = "Outdoor",
                isCompleted = false
            ),
            DateIdea(
                id = 4,
                title = "Pampering & Relaxation Evening",
                description = "Offer a soothing head massage, a relaxing playlist, and zero chores for her tonight.",
                locationType = "Home",
                isCompleted = false
            ),
            DateIdea(
                id = 5,
                title = "Charming Weekend Cafe Visit",
                description = "Dress up nicely, take plenty of lovely photos of her, and explore a newly opened cafe.",
                locationType = "Dining",
                isCompleted = false
            )
        )
        dateIdeaDao.insertInitialDateIdeas(initialIdeas)
    }

    suspend fun ensureTodayCareChecklist(drink: String = "", flower: String = "", food: String = "") {
        val today = LocalDate.now()
        val todayStr = today.format(DateTimeFormatter.ISO_DATE)

        // Only insert if today's items are not yet created
        val existingCount = careDao.getCountForDate(todayStr)
        if (existingCount > 0) return

        val drinkName = if (drink.isNotBlank()) drink else "chai ya coffee"
        val flowerName = if (flower.isNotBlank()) flower else "phool"
        val foodName = if (food.isNotBlank()) food else "pasandeeda khana"

        // Rotating pool of generic husband care tips
        val tipsPool = listOf(
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "compliment",
                title = "Aaj apni biwi ki genuine tareef karein",
                subtitle = "Tell her how lovely she looks or appreciate something wonderful she did",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "checkin",
                title = "Us se poochein 'Aaj din kaisa gaya?'",
                subtitle = "5 minute bina kisi phone ya distraction ke dhyan se sunein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "beverage",
                title = "Uska pasandeeda mashroob banayein",
                subtitle = "Make her favorite $drinkName unasked and serve it with love",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "text_message",
                title = "Bina wajah ke usay ek pyara message bhejein",
                subtitle = "Send a sweet unprompted text telling her she is on your mind",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "quality_time",
                title = "Uske saath 10 minute quality time guzarein",
                subtitle = "Sit together calmly, hold hands, and talk about sweet things",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "chore",
                title = "Ghar ka koi ek kaam bina kahe kar lein",
                subtitle = "Take care of dishes, fold clothes, or tidy the room quietly",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "flower",
                title = "Uske liye $flowerName le aaein",
                subtitle = "Surprise her with her favorite flower or a fresh sweet blossom",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "food",
                title = "$foodName plan ya order karein",
                subtitle = "Surprise her today with her comfort food for dinner",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "hug",
                title = "Ek pyara 20-second hug dein",
                subtitle = "A long, reassuring embrace when meeting to melt stress away",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "smile",
                title = "Uske chehre par muskurahat le aaein",
                subtitle = "Share a funny memory or joke that makes her laugh with whole heart",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "support",
                title = "Uski kisi hobby ya goal ki tareef karein",
                subtitle = "Encourage her dreams and show genuine respect for her thoughts",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "relax",
                title = "Usko comfortable hone aur rest lene dein",
                subtitle = "Offer a soothing head massage or quiet peaceful break",
                isCompleted = false
            )
        )

        // Select 5 rotating items based on day of year
        val dayIndex = today.dayOfYear
        val selectedItems = mutableListOf<DailyCareItem>()
        for (i in 0 until 5) {
            val item = tipsPool[(dayIndex + i * 2) % tipsPool.size]
            selectedItems.add(item.copy(id = 0, dateKey = todayStr))
        }

        careDao.insertCareItems(selectedItems)
    }

    suspend fun toggleCareItem(id: Int, completed: Boolean) {
        careDao.setCareItemCompleted(id, completed)
    }

    // Gifts
    suspend fun addGift(gift: GiftWish) = giftDao.insertGift(gift)
    suspend fun updateGift(gift: GiftWish) = giftDao.updateGift(gift)
    suspend fun deleteGift(gift: GiftWish) = giftDao.deleteGift(gift)

    // Love Notes
    suspend fun addLoveNote(note: LoveNote) = loveNoteDao.insertLoveNote(note)
    suspend fun updateLoveNote(note: LoveNote) = loveNoteDao.updateLoveNote(note)
    suspend fun deleteLoveNote(note: LoveNote) = loveNoteDao.deleteLoveNote(note)

    // Date Ideas
    suspend fun addDateIdea(idea: DateIdea) = dateIdeaDao.insertDateIdea(idea)
    suspend fun updateDateIdea(idea: DateIdea) = dateIdeaDao.updateDateIdea(idea)
    suspend fun deleteDateIdea(idea: DateIdea) = dateIdeaDao.deleteDateIdea(idea)
}
