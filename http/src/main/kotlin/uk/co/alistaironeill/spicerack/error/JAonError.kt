package uk.co.alistaironeill.spicerack.error

import com.ubertob.kondor.json.*
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists

object JAonError : JSealed<AonError>() {
    private const val UNEXPECTED = "Unexpected"
    private const val BAD_REQUEST = "BadRequest"
    private const val ALREADY_EXISTS = "AlreadyExists"
    private const val NOT_FOUND = "NotFound"
    private const val MEMBER_NOT_FOUND = "MemberNotFound"

    override val subConverters: Map<String, ObjectNodeConverter<out AonError>> =
        mapOf(
            ALREADY_EXISTS to JAlreadyExists,
            NOT_FOUND to JNotFound,
            MEMBER_NOT_FOUND to JMemberNotFound,
            BAD_REQUEST to JBadRequest,
            UNEXPECTED to JUnexpectedError
        )

    override fun extractTypeName(obj: AonError) = when (obj) {
        is AlreadyExists -> ALREADY_EXISTS
        is BadRequest.Generic -> BAD_REQUEST
        is UnexpectedError -> UNEXPECTED
        is NotFound.Member -> MEMBER_NOT_FOUND
        is NotFound.Simple -> NOT_FOUND
    }

    object JAlreadyExists : JAny<AlreadyExists>() {
        private val type by str(AlreadyExists::type)
        private val id by str(AlreadyExists::id)
        private val existingType by str(AlreadyExists::existingType)
        private val existingId by str(AlreadyExists::existingId)

        override fun JsonNodeObject.deserializeOrThrow() =
            AlreadyExists(
                +type,
                +id,
                +existingType,
                +existingId
            )
    }

    object JNotFound : JAny<NotFound.Simple>() {
        private val type by str(NotFound.Simple::type)
        private val id by str(NotFound.Simple::id)

        override fun JsonNodeObject.deserializeOrThrow() =
            NotFound(
                +type,
                +id
            )
    }

    object JMemberNotFound : JAny<NotFound.Member>() {
        private val type by str(NotFound.Member::type)
        private val id by str(NotFound.Member::id)
        private val parentType by str(NotFound.Member::parentType)
        private val parentId by str(NotFound.Member::parentId)

        override fun JsonNodeObject.deserializeOrThrow() =
            NotFound(
                +type,
                +id,
                +parentType,
                +parentId
            )
    }

    object JBadRequest : JAny<BadRequest.Generic>() {
        private val msg by str(BadRequest.Generic::msg)
        override fun JsonNodeObject.deserializeOrThrow() = BadRequest.Generic(+msg)
    }

    object JUnexpectedError : JAny<UnexpectedError>() {
        private val code by num(UnexpectedError::code)
        private val message by str(UnexpectedError::message)
        override fun JsonNodeObject.deserializeOrThrow() = UnexpectedError(+code, +message)
    }
}