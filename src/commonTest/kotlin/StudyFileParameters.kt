data class StudyFileParameters(val partID: String, val testNum: Int, val testType: String, val testQualifier: String) {

    val isGaitTask:Boolean = testType == "Walk"

    val testTypeInt:Int = if (isGaitTask) {
        1
    } else {
        if (testType == "Firm") {
            1
        } else {
            2
        }
    }

    val testQualifierInt:Int = if (isGaitTask) {
        if (testQualifier == "HF") {
            1
        } else {
            2
        }
    } else {
        if (testQualifier == "EO") {
            1
        } else {
            2
        }
    }
}