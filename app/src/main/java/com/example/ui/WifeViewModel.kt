package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DailyCareItem
import com.example.data.DateIdea
import com.example.data.GiftWish
import com.example.data.LoveNote
import com.example.data.UserProfile
import com.example.data.WifeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Milestones(
    val daysTogether: Long = 0,
    val yearsCount: Int = 0,
    val daysUntilNextAnniversary: Long = 0,
    val nextAnniversaryNumber: Int = 1,
    val daysUntilNextBirthday: Long = 0,
    val anniversaryDisplay: String = "",
    val birthdayDisplay: String = ""
)

data class WifeUiState(
    val profile: UserProfile? = null,
    val milestones: Milestones = Milestones(),
    val careItems: List<DailyCareItem> = emptyList(),
    val gifts: List<GiftWish> = emptyList(),
    val loveNotes: List<LoveNote> = emptyList(),
    val dateIdeas: List<DateIdea> = emptyList(),
    val isLoading: Boolean = true
)

class WifeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WifeRepository

    val profileFlow: StateFlow<UserProfile?>
    val giftsFlow: StateFlow<List<GiftWish>>
    val loveNotesFlow: StateFlow<List<LoveNote>>
    val dateIdeasFlow: StateFlow<List<DateIdea>>
    val careItemsFlow: StateFlow<List<DailyCareItem>>

    private val _uiState = MutableStateFlow(WifeUiState())
    val uiState: StateFlow<WifeUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = WifeRepository(
            profileDao = database.userProfileDao(),
            giftDao = database.giftWishDao(),
            careDao = database.dailyCareItemDao(),
            loveNoteDao = database.loveNoteDao(),
            dateIdeaDao = database.dateIdeaDao()
        )

        profileFlow = repository.profile.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

        giftsFlow = repository.gifts.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

        loveNotesFlow = repository.loveNotes.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

        dateIdeasFlow = repository.dateIdeas.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

        careItemsFlow = repository.getCareItemsForToday().stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

        viewModelScope.launch {
            repository.ensureInitialContent()
        }

        // Combine flows to produce unified UI state
        viewModelScope.launch {
            combine(
                profileFlow,
                careItemsFlow,
                giftsFlow,
                loveNotesFlow,
                dateIdeasFlow
            ) { profile, careItems, gifts, loveNotes, dateIdeas ->
                val calculatedMilestones = if (profile != null) calculateMilestones(profile) else Milestones()

                // Generate today's checklist if profile exists
                if (profile != null) {
                    repository.ensureTodayCareChecklist(
                        drink = profile.favoriteDrink,
                        flower = profile.favoriteFlower,
                        food = profile.favoriteFood
                    )
                }

                WifeUiState(
                    profile = profile,
                    milestones = calculatedMilestones,
                    careItems = careItems,
                    gifts = gifts,
                    loveNotes = loveNotes,
                    dateIdeas = dateIdeas,
                    isLoading = false
                )
            }.collect { combinedState ->
                _uiState.value = combinedState
            }
        }
    }

    private fun calculateMilestones(profile: UserProfile): Milestones {
        try {
            val today = LocalDate.now()

            // Calculate wedding anniversary
            val weddingDate = if (profile.anniversaryDate.isNotBlank()) {
                LocalDate.parse(profile.anniversaryDate)
            } else {
                today
            }

            val daysTogether = if (today.isAfter(weddingDate) || today.isEqual(weddingDate)) {
                ChronoUnit.DAYS.between(weddingDate, today)
            } else 0L

            var thisYearAnniv = try {
                weddingDate.withYear(today.year)
            } catch (e: Exception) {
                LocalDate.of(today.year, weddingDate.monthValue, weddingDate.dayOfMonth.coerceAtMost(28))
            }

            val nextAnniv = if (thisYearAnniv.isBefore(today)) {
                thisYearAnniv.plusYears(1)
            } else {
                thisYearAnniv
            }
            val daysUntilAnniv = ChronoUnit.DAYS.between(today, nextAnniv)
            val nextAnnivNum = (nextAnniv.year - weddingDate.year).coerceAtLeast(1)
            val yearsTogether = ChronoUnit.YEARS.between(weddingDate, today).toInt().coerceAtLeast(0)

            // Calculate birthday
            val bday = if (profile.birthdayDate.isNotBlank()) {
                LocalDate.parse(profile.birthdayDate)
            } else {
                today
            }

            var thisYearBday = try {
                bday.withYear(today.year)
            } catch (e: Exception) {
                LocalDate.of(today.year, bday.monthValue, bday.dayOfMonth.coerceAtMost(28))
            }

            val nextBday = if (thisYearBday.isBefore(today)) {
                thisYearBday.plusYears(1)
            } else {
                thisYearBday
            }
            val daysUntilBday = ChronoUnit.DAYS.between(today, nextBday)

            val annivDisplay = "${weddingDate.dayOfMonth}/${weddingDate.monthValue}"
            val bdayDisplay = "${bday.dayOfMonth}/${bday.monthValue}"

            return Milestones(
                daysTogether = daysTogether,
                yearsCount = yearsTogether,
                daysUntilNextAnniversary = daysUntilAnniv,
                nextAnniversaryNumber = nextAnnivNum,
                daysUntilNextBirthday = daysUntilBday,
                anniversaryDisplay = annivDisplay,
                birthdayDisplay = bdayDisplay
            )
        } catch (e: Exception) {
            return Milestones()
        }
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            repository.ensureTodayCareChecklist(
                drink = profile.favoriteDrink,
                flower = profile.favoriteFlower,
                food = profile.favoriteFood
            )
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    fun resetProfile() {
        viewModelScope.launch {
            repository.deleteProfile()
        }
    }

    fun toggleCareItem(id: Int, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleCareItem(id, completed)
        }
    }

    fun addGift(title: String, category: String, priceEstimate: String, occasion: String, notes: String) {
        viewModelScope.launch {
            repository.addGift(
                GiftWish(
                    title = title,
                    category = category,
                    priceEstimate = priceEstimate,
                    occasion = occasion,
                    notes = notes,
                    isFulfilled = false
                )
            )
        }
    }

    fun toggleGiftFulfilled(gift: GiftWish) {
        viewModelScope.launch {
            repository.updateGift(gift.copy(isFulfilled = !gift.isFulfilled))
        }
    }

    fun deleteGift(gift: GiftWish) {
        viewModelScope.launch {
            repository.deleteGift(gift)
        }
    }

    fun addLoveNote(title: String, message: String, hindiText: String, category: String) {
        viewModelScope.launch {
            repository.addLoveNote(
                LoveNote(
                    title = title,
                    message = message,
                    hindiText = hindiText,
                    category = category,
                    isFavorite = true,
                    isUserCreated = true
                )
            )
        }
    }

    fun toggleNoteFavorite(note: LoveNote) {
        viewModelScope.launch {
            repository.updateLoveNote(note.copy(isFavorite = !note.isFavorite))
        }
    }

    fun deleteLoveNote(note: LoveNote) {
        viewModelScope.launch {
            repository.deleteLoveNote(note)
        }
    }

    fun addDateIdea(title: String, description: String, locationType: String) {
        viewModelScope.launch {
            repository.addDateIdea(
                DateIdea(
                    title = title,
                    description = description,
                    locationType = locationType,
                    isCompleted = false,
                    isUserCreated = true
                )
            )
        }
    }

    fun toggleDateIdeaCompleted(idea: DateIdea) {
        viewModelScope.launch {
            repository.updateDateIdea(idea.copy(isCompleted = !idea.isCompleted))
        }
    }

    fun deleteDateIdea(idea: DateIdea) {
        viewModelScope.launch {
            repository.deleteDateIdea(idea)
        }
    }
}
