package uk.co.alistaironeill.spicerack.error


sealed interface BadRequest : AonError {
    data class Generic(override val msg: String) : BadRequest

    data class AlreadyExists(
        val type: String,
        val id: String,
        val existingType: String,
        val existingId: String
    ) : BadRequest {
        override val msg: String = "$type[$id] already exists as $existingType[$existingId]"

        companion object
    }
}
