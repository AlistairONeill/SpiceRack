package uk.co.alistaironeill.spicerack.slot

import com.ubertob.kondor.json.JMap
import com.ubertob.kondor.json.JSet
import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Method.*
import org.http4k.lens.Path
import uk.co.alistaironeill.spicerack.error.perform
import uk.co.alistaironeill.spicerack.error.toLens
import uk.co.alistaironeill.spicerack.error.toResponse
import uk.co.alistaironeill.spicerack.http.int
import uk.co.alistaironeill.spicerack.model.JLed
import uk.co.alistaironeill.spicerack.model.JSlotString
import uk.co.alistaironeill.spicerack.model.Slot
import uk.co.alistaironeill.spicerack.source.LedGroupSource

object LedGroupSourceHttpHandler {
    const val SLOT = "/api/slot"

    private val xPath = Path.int(Slot.Index).of("x")
    private val yPath = Path.int(Slot.Index).of("y")

    private val slotPath = SLOT / xPath / yPath
    private val ledLens = JLed.toLens()

    fun LedGroupSource.toContractRoutes(): List<ContractRoute> =
        listOf(
            getBySlot(),
            getAll(),
            add(),
            remove()
        )

    private fun LedGroupSource.getBySlot() =
        slotPath meta {

        } bindContract GET to { x, y ->
            {
                perform {
                    get(Slot(x, y))
                }.toResponse(JSet(JLed))
            }
        }

    private fun LedGroupSource.getAll() =
        SLOT meta {

        } bindContract GET to { _ ->
            perform(this::get)
                .toResponse(JMap(JSlotString, JSet(JLed)))
        }

    private fun LedGroupSource.add() =
        slotPath meta {

        } bindContract PUT to { x, y ->
            { request ->
                ledLens(request)
                    .perform { add(Slot(x, y), this) }
                    .toResponse()
            }
        }

    private fun LedGroupSource.remove() =
        slotPath meta {

        } bindContract DELETE to { x, y ->
            { request ->
                ledLens(request)
                    .perform { remove(Slot(x, y), this) }
                    .toResponse()
            }
        }
}