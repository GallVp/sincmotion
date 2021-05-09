expect class StudyData() {
    val studyNames: List<String>
    fun validateStudyData(forStudy: String, testTol: Double, sampleSize: Int = 0)
    fun validateStudyFile(forStudy: String, fileName:String, testTol: Double)
}

/**
 * Include n = sampleSize studies if sampleSize != 0 else include all studies.
 */
fun StudyData.validateAllStudies(testTol: Double, sampleSize: Int = 0) {
    studyNames.forEach {
        println("Running tests for study: $it")
        validateStudyData(it, testTol, sampleSize)
        println("All tests passed on study: $it")
    }
}