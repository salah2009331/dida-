package com.example.data

import android.content.Context
import com.example.model.DentalCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class CaseRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("doctor_dida_prefs", Context.MODE_PRIVATE)
    private var cachedCases: List<DentalCase> = emptyList()

    suspend fun loadCases(): List<DentalCase> = withContext(Dispatchers.IO) {
        if (cachedCases.isNotEmpty()) return@withContext cachedCases

        val favoritesSet = prefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()
        val casesList = mutableListOf<DentalCase>()

        try {
            val inputStream = context.assets.open("dental_cases.json")
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val jsonString = reader.readText()
            reader.close()

            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val spec = obj.optString("spec", "")
                val specLabel = obj.optString("spec_label", "")
                val ageGroup = obj.optString("age_group", "")
                val ageGroupLabel = obj.optString("age_group_label", "")
                val patientAge = obj.optInt("patient_age", 25)
                val tooth = obj.optString("tooth", "11")
                val diff = obj.optString("diff", "متوسطة")
                val title = obj.optString("title", "")
                val summary = obj.optString("summary", "")
                val diagnosis = obj.optString("diagnosis", "")
                val pitfalls = obj.optString("pitfalls", "")

                val planArray = obj.optJSONArray("plan")
                val planList = mutableListOf<String>()
                if (planArray != null) {
                    for (p in 0 until planArray.length()) {
                        planList.add(planArray.getString(p))
                    }
                }

                val isFav = favoritesSet.contains(id.toString())
                val note = prefs.getString("note_$id", "") ?: ""

                casesList.add(
                    DentalCase(
                        id = id,
                        spec = spec,
                        specLabel = specLabel,
                        ageGroup = ageGroup,
                        ageGroupLabel = ageGroupLabel,
                        patientAge = patientAge,
                        tooth = tooth,
                        diff = diff,
                        title = title,
                        summary = summary,
                        diagnosis = diagnosis,
                        plan = planList,
                        pitfalls = pitfalls,
                        isFavorite = isFav,
                        userNotes = note
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cachedCases = casesList
        casesList
    }

    fun toggleFavorite(caseId: Int): Boolean {
        val currentFavs = prefs.getStringSet("favorite_ids", emptySet())?.toMutableSet() ?: mutableSetOf()
        val idStr = caseId.toString()
        val isNowFav = if (currentFavs.contains(idStr)) {
            currentFavs.remove(idStr)
            false
        } else {
            currentFavs.add(idStr)
            true
        }
        prefs.edit().putStringSet("favorite_ids", currentFavs).apply()

        // Update cache
        cachedCases = cachedCases.map {
            if (it.id == caseId) it.copy(isFavorite = isNowFav) else it
        }
        return isNowFav
    }

    fun saveUserNote(caseId: Int, note: String) {
        prefs.edit().putString("note_$caseId", note).apply()
        cachedCases = cachedCases.map {
            if (it.id == caseId) it.copy(userNotes = note) else it
        }
    }

    fun getRandomCase(
        spec: String? = null,
        difficulty: String? = null,
        ageGroup: String? = null
    ): DentalCase? {
        val filtered = cachedCases.filter { c ->
            (spec == null || spec == "all" || c.spec == spec) &&
            (difficulty == null || difficulty == "all" || c.diff == difficulty) &&
            (ageGroup == null || ageGroup == "all" || c.ageGroup == ageGroup)
        }
        return if (filtered.isNotEmpty()) filtered.random() else cachedCases.randomOrNull()
    }

    fun getDiagnosisDistractors(correctDiagnosis: String, count: Int = 3): List<String> {
        val allOtherDiagnoses = cachedCases
            .map { it.title }
            .distinct()
            .filter { it != correctDiagnosis }

        return allOtherDiagnoses.shuffled().take(count)
    }
}
