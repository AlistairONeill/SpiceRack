package uk.co.alistaironeill.spicerack.domain.error

data class NotFound(val type: String, val id: String): AonError {
    override val msg = "Could not find $type[$id]"
}