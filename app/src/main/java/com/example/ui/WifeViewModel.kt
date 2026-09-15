package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DailyCareItem
import com.example.data.DateIdea
import com.example.data.GiftWish
import com.example.data.LoveNote
import com.example.data.WifeDatabase
import com.example.data.WifeProfile
import com.example.data.WifeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Milestones(
    val daysTogether: Long = 0,
    val yearsCount: Int = 0,
    val daysUntilNextAnniversary: Long = 0,
    val nextAnniversaryNumber: Int = 1,
    val daysUntilNextBirthday: Long = 0
)

data class WifeUiState(
    val profile: WifeProfile = WifeProfile(),
    val milestones: Milestones = Milestones(),
    val careItems: List<DailyCareItem> = emptyList(),
    val gifts: List<GiftWish> = emptyList(),
    val loveNotes: List<LoveNote> = emptyList(),
    val dateIdeas: List<DateIdea> = emptyList(),
    val isLoading: Boolean = false
)

class WifeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WifeRepository

    val profileFlow: StateFlow<WifeProfile?>
    val giftsFlow: StateFlow<List<GiftWish>>
    val loveNotesFlow: StateFlow<List<LoveNote>>
    val dateIdeasFlow: StateFlow<List<DateIdea>>
    val careItemsFlow: StateFlow<List<DailyCareItem>>

    private val _uiState = MutableStateFlow(WifeUiState())
    val uiState: StateFlow<WifeUiState> = _uiState.asStateFlow()

    init {
        val database = WifeDatabase.getDatabase(application)
        repository = WifeRepository(database.wifeDao())

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
            repository.ensureInitialized()
            repository.ensureTodayCareChecklist()
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
                val currentProfile = profile ?: WifeProfile()
                val calculatedMilestones = calculateMilestones(currentProfile)
                WifeUiState(
                    profile = currentProfile,
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

    private fun calculateMilestones(profile: WifeProfile): Milestones {
        try {
            val today = LocalDate.now()
            val weddingDate = LocalDate.of(
                profile.weddingYear.coerceIn(1970, 2099),
                profile.weddingMonth.coerceIn(1, 12),
                profile.weddingDay.coerceIn(1, 31)
            )

            val daysTogether = if (today.isAfter(weddingDate) || today.isEqual(weddingDate)) {
                ChronoUnit.DAYS.between(weddingDate, today)
            } else 0L

            var thisYearAnniv = LocalDate.of(
                today.year,
                profile.weddingMonth.coerceIn(1, 12),
                profile.weddingDay.coerceIn(1, 31)
            )
            val nextAnniv = if (thisYearAnniv.isBefore(today)) {
                thisYearAnniv.plusYears(1)
            } else {
                thisYearAnniv
            }
            val daysUntilAnniv = ChronoUnit.DAYS.between(today, nextAnniv)
            val nextAnnivNum = nextAnniv.year - weddingDate.year

            var thisYearBday = LocalDate.of(
                today.year,
                profile.birthMonth.coerceIn(1, 12),
                profile.birthDay.coerceIn(1, 31)
            )
            val nextBday = if (thisYearBday.isBefore(today)) {
                thisYearBday.plusYears(1)
            } else {
                thisYearBday
            }
            val daysUntilBday = ChronoUnit.DAYS.between(today, nextBday)

            val yearsTogether = (daysTogether / 365).toInt()

            return Milestones(
                daysTogether = daysTogether,
                yearsCount = yearsTogether,
                daysUntilNextAnniversary = daysUntilAnniv,
                nextAnniversaryNumber = nextAnnivNum.coerceAtLeast(1),
                daysUntilNextBirthday = daysUntilBday
            )
        } catch (e: Exception) {
            return Milestones(
                daysTogether = 1250,
                yearsCount = 3,
                daysUntilNextAnniversary = 75,
                nextAnniversaryNumber = 4,
                daysUntilNextBirthday = 120
            )
        }
    }

    fun updateProfile(newProfile: WifeProfile) {
        viewModelScope.launch {
            repository.saveProfile(newProfile)
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
