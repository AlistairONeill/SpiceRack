package uk.co.alistaironeill.spicerack.error


sealed interface NotFound: AonError {
    data class Simple internal constructor(val type: String, val id: String): NotFound {
        override val msg = "Could not find $type[$id]"
    }

    data class Member internal constructor(val type: String, val id: String, val parentType: String, val parentId: String):
        NotFound {
        override val msg = "Could not find $type[$id] on $parentType[$parentId]"
    }

    companion object {
        operator fun invoke(type: String, id: String) = Simple(type, id)
        operator fun invoke(type: String, id: String, parentType: String, parentId: String) = Member(type, id, parentType, parentId)
    }
}
