package uk.co.alistaironeill.spicerack.error

data class UnexpectedError(
    val code: Int,
    val message: String
): AonError {
    override val msg = "Error[$code]: $message"
}