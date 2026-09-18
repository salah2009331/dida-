package com.example.model

data class DentalCase(
    val id: Int,
    val spec: String,
    val specLabel: String,
    val ageGroup: String,
    val ageGroupLabel: String,
    val patientAge: Int,
    val tooth: String,
    val diff: String,
    val title: String,
    val summary: String,
    val diagnosis: String,
    val plan: List<String>,
    val pitfalls: String,
    val xrayType: String = "periapical",
    val xrayName: String = "أشعة ذروية (Periapical Radiograph)",
    val xrayFindings: String = "",
    val isFavorite: Boolean = false,
    val userNotes: String = ""
) {
    // Returns quadrant number (1, 2, 3, 4) from FDI (e.g., "44" -> 4)
    val quadrant: Int
        get() = tooth.firstOrNull()?.digitToIntOrNull() ?: 1

    // Returns tooth position within quadrant (1 to 8)
    val toothIndex: Int
        get() = tooth.lastOrNull()?.digitToIntOrNull() ?: 1

    val toothNameArabic: String
        get() {
            val qName = when (quadrant) {
                1 -> "العلوي الأيمن"
                2 -> "العلوي الأيسر"
                3 -> "السفلي الأيسر"
                4 -> "السفلي الأيمن"
                else -> ""
            }
            val tName = when (toothIndex) {
                1 -> "القاطع المركزي"
                2 -> "القاطع الجانبي"
                3 -> "الناب"
                4 -> "الضاحك الأول"
                5 -> "الضاحك الثاني"
                6 -> "الرحى (الضرس) الأولى"
                7 -> "الرحى (الضرس) الثانية"
                8 -> "الرحى الثالثة (ضرس العقل)"
                else -> "سن رقم $toothIndex"
            }
            return "$tName $qName (#$tooth)"
        }

    val toothNameEnglish: String
        get() {
            val qName = when (quadrant) {
                1 -> "Upper Right"
                2 -> "Upper Left"
                3 -> "Lower Left"
                4 -> "Lower Right"
                else -> ""
            }
            val tName = when (toothIndex) {
                1 -> "Central Incisor"
                2 -> "Lateral Incisor"
                3 -> "Canine"
                4 -> "1st Premolar"
                5 -> "2nd Premolar"
                6 -> "1st Molar"
                7 -> "2nd Molar"
                8 -> "3rd Molar (Wisdom)"
                else -> "Tooth $toothIndex"
            }
            return "$qName $tName (FDI $tooth)"
        }
}

enum class DentalSpecialty(val id: String, val arabicName: String, val englishName: String, val iconName: String) {
    ALL("all", "الكل", "All Specialties", "medical_services"),
    ENDO("endo", "علاج الجذور واللبية", "Endodontics", "water_drop"),
    PERIO("perio", "أمراض وجراحة اللثة", "Periodontics", "healing"),
    SURGERY("surgery", "جراحة الفم والأسنان", "Oral Surgery", "cut"),
    ORTHO("ortho", "تقويم الأسنان", "Orthodontics", "architecture"),
    RESTOR("restor", "طب الأسنان التحفظي والحشوات", "Restorative Dentistry", "brush"),
    PEDO("pedo", "طب أسنان الأطفال", "Pediatric Dentistry", "child_care"),
    PROSTHO("prostho", "الاستعاضة والتركيبات", "Prosthodontics", "handyman"),
    RADIO("radio", "أشعة الفم والوجه والفكين", "Oral Radiology", "camera")
}
