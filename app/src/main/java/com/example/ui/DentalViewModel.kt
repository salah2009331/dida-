package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CaseRepository
import com.example.model.DentalCase
import com.example.model.DentalSpecialty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppNavDestination(val titleAr: String, val titleEn: String) {
    SIMULATOR("محاكي الحالات", "Case Simulator"),
    BANK("بنك الحالات", "Case Bank (1000)"),
    QUIZ("اختبار سريري", "Clinical Quiz"),
    FDI_GUIDE("دليل ترقيم FDI", "FDI Chart Guide")
}

data class QuizQuestion(
    val caseItem: DentalCase,
    val options: List<String>,
    val correctIndex: Int,
    var selectedOptionIndex: Int? = null,
    var isSubmitted: Boolean = false
)

data class DentalUiState(
    val isLoading: Boolean = true,
    val currentDestination: AppNavDestination = AppNavDestination.SIMULATOR,
    val allCases: List<DentalCase> = emptyList(),

    // Simulator State
    val currentSimulatorCase: DentalCase? = null,
    val simulatorSpecialtyFilter: String = "all",
    val simulatorDifficultyFilter: String = "all",
    val simulatorAgeGroupFilter: String = "all",
    val isDiagnosisRevealed: Boolean = false,
    val isPlanRevealed: Boolean = false,
    val isPitfallsRevealed: Boolean = false,
    val simulatorGuessOptions: List<String> = emptyList(),
    val simulatorSelectedGuessIndex: Int? = null,
    val simulatorGuessFeedback: String? = null,

    // Case Bank Search & Filter
    val bankSearchQuery: String = "",
    val bankSpecialtyFilter: String = "all",
    val bankDifficultyFilter: String = "all",
    val bankSelectedToothFilter: String? = null,
    val bankOnlyFavorites: Boolean = false,
    val selectedDetailCase: DentalCase? = null,

    // Quiz State
    val isQuizActive: Boolean = false,
    val quizQuestions: List<QuizQuestion> = emptyList(),
    val currentQuizIndex: Int = 0,
    val quizScore: Int = 0,
    val isQuizFinished: Boolean = false,

    // FDI Tooth chart selection for reference
    val selectedFdiToothInfo: String? = "11"
)

class DentalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CaseRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(DentalUiState())
    val uiState: StateFlow<DentalUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cases = repository.loadCases()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    allCases = cases
                )
            }
            generateNewSimulatorCase()
        }
    }

    fun navigateTo(destination: AppNavDestination) {
        _uiState.update { it.copy(currentDestination = destination) }
    }

    // --- Simulator Logic ---
    fun generateNewSimulatorCase() {
        val state = _uiState.value
        val newCase = repository.getRandomCase(
            spec = state.simulatorSpecialtyFilter,
            difficulty = state.simulatorDifficultyFilter,
            ageGroup = state.simulatorAgeGroupFilter
        )

        val distractors = if (newCase != null) {
            val wrong = repository.getDiagnosisDistractors(newCase.title, count = 3)
            (wrong + newCase.title).shuffled()
        } else emptyList()

        _uiState.update {
            it.copy(
                currentSimulatorCase = newCase,
                isDiagnosisRevealed = false,
                isPlanRevealed = false,
                isPitfallsRevealed = false,
                simulatorGuessOptions = distractors,
                simulatorSelectedGuessIndex = null,
                simulatorGuessFeedback = null
            )
        }
    }

    fun submitSimulatorGuess(index: Int) {
        val state = _uiState.value
        val currentCase = state.currentSimulatorCase ?: return
        val chosen = state.simulatorGuessOptions.getOrNull(index) ?: return

        val isCorrect = chosen == currentCase.title
        val feedback = if (isCorrect) {
            "ممتاز! تشخيص دقيق وصحيح 🎯"
        } else {
            "تشخيص غير مطابق للحالة. تفقد التشخيص الصحيح وخطة العلاج أدناه 👇"
        }

        _uiState.update {
            it.copy(
                simulatorSelectedGuessIndex = index,
                simulatorGuessFeedback = feedback,
                isDiagnosisRevealed = true,
                isPlanRevealed = true,
                isPitfallsRevealed = true
            )
        }
    }

    fun setSimulatorSpecialtyFilter(spec: String) {
        _uiState.update { it.copy(simulatorSpecialtyFilter = spec) }
        generateNewSimulatorCase()
    }

    fun setSimulatorDifficultyFilter(diff: String) {
        _uiState.update { it.copy(simulatorDifficultyFilter = diff) }
        generateNewSimulatorCase()
    }

    fun setSimulatorAgeGroupFilter(age: String) {
        _uiState.update { it.copy(simulatorAgeGroupFilter = age) }
        generateNewSimulatorCase()
    }

    fun toggleRevealDiagnosis() {
        _uiState.update { it.copy(isDiagnosisRevealed = !it.isDiagnosisRevealed) }
    }

    fun toggleRevealPlan() {
        _uiState.update { it.copy(isPlanRevealed = !it.isPlanRevealed) }
    }

    fun toggleRevealPitfalls() {
        _uiState.update { it.copy(isPitfallsRevealed = !it.isPitfallsRevealed) }
    }

    fun toggleFavorite(caseItem: DentalCase) {
        val newFav = repository.toggleFavorite(caseItem.id)
        val updatedCases = _uiState.value.allCases.map {
            if (it.id == caseItem.id) it.copy(isFavorite = newFav) else it
        }
        val updatedCurrent = if (_uiState.value.currentSimulatorCase?.id == caseItem.id) {
            _uiState.value.currentSimulatorCase?.copy(isFavorite = newFav)
        } else _uiState.value.currentSimulatorCase

        val updatedDetail = if (_uiState.value.selectedDetailCase?.id == caseItem.id) {
            _uiState.value.selectedDetailCase?.copy(isFavorite = newFav)
        } else _uiState.value.selectedDetailCase

        _uiState.update {
            it.copy(
                allCases = updatedCases,
                currentSimulatorCase = updatedCurrent,
                selectedDetailCase = updatedDetail
            )
        }
    }

    fun saveCaseNote(caseId: Int, note: String) {
        repository.saveUserNote(caseId, note)
        val updatedCases = _uiState.value.allCases.map {
            if (it.id == caseId) it.copy(userNotes = note) else it
        }
        val updatedCurrent = if (_uiState.value.currentSimulatorCase?.id == caseId) {
            _uiState.value.currentSimulatorCase?.copy(userNotes = note)
        } else _uiState.value.currentSimulatorCase

        val updatedDetail = if (_uiState.value.selectedDetailCase?.id == caseId) {
            _uiState.value.selectedDetailCase?.copy(userNotes = note)
        } else _uiState.value.selectedDetailCase

        _uiState.update {
            it.copy(
                allCases = updatedCases,
                currentSimulatorCase = updatedCurrent,
                selectedDetailCase = updatedDetail
            )
        }
    }

    // --- Case Bank Filters ---
    fun setBankSearchQuery(query: String) {
        _uiState.update { it.copy(bankSearchQuery = query) }
    }

    fun setBankSpecialtyFilter(spec: String) {
        _uiState.update { it.copy(bankSpecialtyFilter = spec) }
    }

    fun setBankDifficultyFilter(diff: String) {
        _uiState.update { it.copy(bankDifficultyFilter = diff) }
    }

    fun setBankToothFilter(tooth: String?) {
        _uiState.update {
            it.copy(bankSelectedToothFilter = if (it.bankSelectedToothFilter == tooth) null else tooth)
        }
    }

    fun toggleBankOnlyFavorites() {
        _uiState.update { it.copy(bankOnlyFavorites = !it.bankOnlyFavorites) }
    }

    fun selectDetailCase(caseItem: DentalCase?) {
        _uiState.update { it.copy(selectedDetailCase = caseItem) }
    }

    // --- Quiz Sprint Logic ---
    fun startQuiz(questionCount: Int = 5) {
        val pool = _uiState.value.allCases.shuffled().take(questionCount)
        val questions = pool.map { caseItem ->
            val distractors = repository.getDiagnosisDistractors(caseItem.title, count = 3)
            val allOptions = (distractors + caseItem.title).shuffled()
            val correctIdx = allOptions.indexOf(caseItem.title)
            QuizQuestion(
                caseItem = caseItem,
                options = allOptions,
                correctIndex = correctIdx
            )
        }

        _uiState.update {
            it.copy(
                isQuizActive = true,
                quizQuestions = questions,
                currentQuizIndex = 0,
                quizScore = 0,
                isQuizFinished = false
            )
        }
    }

    fun answerQuizQuestion(optionIndex: Int) {
        val state = _uiState.value
        val questions = state.quizQuestions.toMutableList()
        val currentQ = questions.getOrNull(state.currentQuizIndex) ?: return

        if (currentQ.isSubmitted) return

        currentQ.selectedOptionIndex = optionIndex
        currentQ.isSubmitted = true
        val isCorrect = optionIndex == currentQ.correctIndex
        val newScore = if (isCorrect) state.quizScore + 1 else state.quizScore

        _uiState.update {
            it.copy(
                quizQuestions = questions,
                quizScore = newScore
            )
        }
    }

    fun nextQuizQuestion() {
        val state = _uiState.value
        if (state.currentQuizIndex + 1 < state.quizQuestions.size) {
            _uiState.update { it.copy(currentQuizIndex = it.currentQuizIndex + 1) }
        } else {
            _uiState.update { it.copy(isQuizFinished = true) }
        }
    }

    // --- FDI Reference ---
    fun selectFdiToothForInfo(tooth: String) {
        _uiState.update { it.copy(selectedFdiToothInfo = tooth) }
    }
}
