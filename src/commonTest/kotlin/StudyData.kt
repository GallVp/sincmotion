expect class StudyData() {
    val studyNames: List<String>
    fun validateStudyData(forStudy:String, testTol:Double)
}

fun StudyData.validateAllStudies(testTol:Double) {
    studyNames.forEach {
        println("Running tests for study: $it")
        validateStudyData(it, testTol)
        println("All tests passed on study: $it")
    }
}