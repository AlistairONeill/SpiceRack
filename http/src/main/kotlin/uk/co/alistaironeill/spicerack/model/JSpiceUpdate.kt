package uk.co.alistaironeill.spicerack.model

import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.JSealed
import com.ubertob.kondor.json.ObjectNodeConverter
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import com.ubertob.kondor.json.obj
import uk.co.alistaironeill.spicerack.json.str
import uk.co.alistaironeill.spicerack.model.SpiceUpdate.*

object JSpiceUpdate : JSealed<SpiceUpdate>() {
    private const val RENAME = "Rename"
    private const val ADD_ALIAS = "AddAlias"
    private const val REMOVE_ALIAS = "RemoveAlias"
    private const val SET_COLOUR = "SetColour"

    override val subConverters: Map<String, ObjectNodeConverter<out SpiceUpdate>> =
        mapOf(
            RENAME to JRename,
            ADD_ALIAS to JAddAlias,
            REMOVE_ALIAS to JRemoveAlias,
            SET_COLOUR to JSetColour
        )

    override fun extractTypeName(obj: SpiceUpdate) =
        when (obj) {
            is AddAlias -> ADD_ALIAS
            is RemoveAlias -> REMOVE_ALIAS
            is Rename -> RENAME
            is SetColour -> SET_COLOUR
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

    internal object JSetColour : JAny<SetColour>() {
        private val colour by obj(JRGB, SetColour::colour)
        override fun JsonNodeObject.deserializeOrThrow() = SetColour(+colour)
    }
}