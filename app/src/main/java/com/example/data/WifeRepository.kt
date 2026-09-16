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
        // Seed universal romantic love notes in Roman Urdu
        val initialNotes = listOf(
            LoveNote(
                id = 1,
                title = "Meri Zindagi Ki Roshni",
                hindiText = "Har subah tumhari muskaan dekh kar lagta hai ke maine zindagi mein koi bahut pyara kaam kiya tha.",
                message = "Tum meri zindagi ka sab se pyara aur khoobsurat hissa ho. Har din tumhari muskurahat mere liye ek nayi barkat hai.",
                category = "Mohabbat",
                isFavorite = true,
                isUserCreated = false
            ),
            LoveNote(
                id = 2,
                title = "Sukoon Aur Pyar",
                hindiText = "Duniya ki saari bheed ek taraf, aur shaam ko tumhare saath baith kar chai peena ek taraf.",
                message = "Tumhare paas aate hi saari thakan door ho jaati hai. Tumhara saath mere dil ka asal sukoon hai.",
                category = "Shukriya",
                isFavorite = true,
                isUserCreated = false
            ),
            LoveNote(
                id = 3,
                title = "Khoobsurat Muskaan",
                hindiText = "Tum jab dil khol kar hasti ho, duniya ki sab se pyari lagti ho.",
                message = "Tumhare chehre ki khushi meri sab se badi daulat hai. Hamesha yunhi hasti muskurati raho.",
                category = "Tareef",
                isFavorite = false,
                isUserCreated = false
            ),
            LoveNote(
                id = 4,
                title = "Meri Sab Se Achhi Dost",
                hindiText = "Har mod par mera saath dene ke liye shukriya. Tum sirf meri biwi nahi, meri sab se achhi dost ho.",
                message = "Har mushkil aur aasan waqt mein mera sahara banne ke liye bohot shukriya. Tumhare bina sab soona lagta hai.",
                category = "Shukriya",
                isFavorite = false,
                isUserCreated = false
            ),
            LoveNote(
                id = 5,
                title = "Dil Se Maafi",
                hindiText = "Agar meri kisi baat ya bhool se tumhara dil dukha ho, toh mujhe dil se maaf kar dena.",
                message = "Tumhara dil dukhana mera maqsad kabhi nahi ho sakta. Tumhari khushi mere liye sab se zyada ahem hai.",
                category = "Maafi",
                isFavorite = false,
                isUserCreated = false
            )
        )
        loveNoteDao.insertInitialNotes(initialNotes)

        // Seed inspiring date ideas in Roman Urdu
        val initialIdeas = listOf(
            DateIdea(
                id = 1,
                title = "Shaam Ki Chai Aur Long Drive",
                description = "Unhe achanak surprise le kar niklein, unki pasandeeda dhun lagayein aur kisi pur-sukoon jagah garam chai piyein.",
                locationType = "Bahar",
                isCompleted = false
            ),
            DateIdea(
                id = 2,
                title = "Ghar Par Candlelight Dinner",
                description = "Unka pasandeeda khana banayein ya mangwayein, battiyan maddham karein aur sukoon se baatein karein.",
                locationType = "Ghar Par",
                isCompleted = false
            ),
            DateIdea(
                id = 3,
                title = "Late Night Ice Cream Ya Mithai",
                description = "Raat ko aaramdeh kapron mein chupke se nikal kar unki pasandeeda ice cream ya koi meethi cheez khayein.",
                locationType = "Bahar",
                isCompleted = false
            ),
            DateIdea(
                id = 4,
                title = "Aaram Aur Sukoon Bhari Shaam",
                description = "Aaj unhe ghar ke kisi kaam ki fikar na hone dein, halka phulka head massage dein aur sukoon bhara mahol banayein.",
                locationType = "Ghar Par",
                isCompleted = false
            ),
            DateIdea(
                id = 5,
                title = "Weekend Par Naye Cafe Ka Chakkar",
                description = "Ache se taiyar hokar unki khoobsurat tasweerein banayein aur kisi pyare se cafe mein waqt guzarein.",
                locationType = "Khana Peena",
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

        // Rotating pool of husband care tips in Roman Urdu
        val tipsPool = listOf(
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "compliment",
                title = "Aaj apni biwi ki dil se tareef karein",
                subtitle = "Unhe batayein ke woh kitni pyari lag rahi hain aur unki qadar karein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "checkin",
                title = "Unse poochein 'Aaj din kaisa gaya?'",
                subtitle = "5 minute bina kisi phone ya distraction ke dhyan se sunein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "beverage",
                title = "Unka pasandeeda mashroob banayein",
                subtitle = "Bina kahe unke liye $drinkName banayein aur pyar se pesh karein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "text_message",
                title = "Bina wajah ke unhe ek pyara message bhejein",
                subtitle = "Din ke dauran ek chhota sa paigham bhejein ke woh aapke dil mein hain",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "quality_time",
                title = "Unke saath 10 minute quality time guzarein",
                subtitle = "Bina kisi kaam ke sirf unke paas baithein aur meethi baatein karein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "chore",
                title = "Ghar ka koi ek kaam bina kahe kar lein",
                subtitle = "Bartan, safai ya koi bhi zaroori kaam khud aage badh kar sambhal lein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "flower",
                title = "Unke liye $flowerName le aaein",
                subtitle = "Ghar aate waqt unki pasand ka phool ya guldasta la kar surprise dein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "food",
                title = "$foodName plan ya order karein",
                subtitle = "Aaj raat ke khane mein unka pasandeeda khana pesh karein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "hug",
                title = "Ek pyara 20-second hug dein",
                subtitle = "Ghar laut'te hi sukoon se gale lagayein taake din bhar ki thakan mit jaye",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "smile",
                title = "Unke chehre par muskurahat le aaein",
                subtitle = "Koi purani hansi mazaq wali baat ya pyari yaad dohra kar unhe hasayein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "support",
                title = "Uski kisi hobby ya koshish ki tareef karein",
                subtitle = "Unke khwabon aur shauq ki hosla afzai karein aur qadar karein",
                isCompleted = false
            ),
            DailyCareItem(
                dateKey = todayStr,
                taskKey = "relax",
                title = "Unhe aaram karne ka pura waqt dein",
                subtitle = "Unhe chai bana kar dein aur kahein ke aaj thodi der sukoon karein",
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
