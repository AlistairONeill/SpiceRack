package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.JSealed
import com.ubertob.kondor.json.ObjectNodeConverter
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import uk.co.alistaironeill.spicerack.json.str
import uk.co.alistaironeill.spicerack.spice.SpiceUpdate.*

object JSpiceUpdate : JSealed<SpiceUpdate>() {
    private const val RENAME = "Rename"
    private const val ADD_ALIAS = "AddAlias"
    private const val REMOVE_ALIAS = "RemoveAlias"

    override val subConverters: Map<String, ObjectNodeConverter<out SpiceUpdate>> =
        mapOf(
            RENAME to JRename,
            ADD_ALIAS to JAddAlias,
            REMOVE_ALIAS to JRemoveAlias
        )

    override fun extractTypeName(obj: SpiceUpdate) =
        when (obj) {
            is AddAlias -> ADD_ALIAS
            is RemoveAlias -> REMOVE_ALIAS
            is Rename -> RENAME
        }

    internal object JRename : JAny<Rename>() {
        private val name by str(Rename::name)
        override fun JsonNodeObject.deserializeOrThrow() = Rename(+name)
    }

    internal object JAddAlias : JAny<AddAlias>() {
        private val alias by str(AddAlias::alias)
        override fun JsonNodeObject.deserializeOrThrow() = AddAlias(+alias)
    }

    internal object JRemoveAlias : JAny<RemoveAlias>() {
        private val alias by str(RemoveAlias::alias)
        override fun JsonNodeObject.deserializeOrThrow() = RemoveAlias(+alias)
    }
}