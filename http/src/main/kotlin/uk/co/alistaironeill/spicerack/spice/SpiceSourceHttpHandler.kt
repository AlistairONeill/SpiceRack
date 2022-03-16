package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.json.JSet
import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Method.*
import org.http4k.lens.Path
import uk.co.alistaironeill.spicerack.error.perform
import uk.co.alistaironeill.spicerack.error.toResponse
import uk.co.alistaironeill.spicerack.http.str

object SpiceSourceHttpHandler {
    const val SPICE_PATH = "/api/spice"
    const val SPICE_NAME_PATH = "/api/spiceName"

    private val idPath = Path.str(SpiceId).of("SpiceId")
    private val namePath = Path.str(SpiceName).of("SpiceName")

    fun SpiceSource.toContractRoutes(): List<ContractRoute> =
        listOf(
            getAll(),
            getById(),
            getByName(),
            create(),
            addAlias(),
            removeAlias(),
            remove()
        )

    private fun SpiceSource.getAll() =
        SPICE_PATH meta {

        } bindContract GET to { _ ->
            perform { get() }
                .toResponse(JSet(JSpice))
        }

    private fun SpiceSource.getById() =
        SPICE_PATH / idPath meta {

        } bindContract GET to { id ->
            {
                perform { get(id) }
                    .toResponse(JSpice)
            }
        }

    private fun SpiceSource.getByName() =
        SPICE_NAME_PATH / namePath meta {

        } bindContract GET to { name ->
            {
                perform { get(name) }
                    .toResponse(JSpice)
            }
        }

    private fun SpiceSource.create() =
        SPICE_NAME_PATH / namePath meta {

        } bindContract PUT to { name ->
            {
                perform { create(name) }
                    .toResponse(JSpice)
            }
        }

    private fun SpiceSource.addAlias() =
        SPICE_PATH / idPath / namePath meta {

        } bindContract PUT to { id, name ->
            {
                perform { addAlias(id, name) }
                    .toResponse()
            }
        }

    private fun SpiceSource.removeAlias() =
        SPICE_PATH / idPath / namePath meta {

        } bindContract DELETE to { id, name ->
            {
                perform { removeAlias(id, name) }
                    .toResponse()
            }
        }

    private fun SpiceSource.remove() =
        SPICE_PATH / idPath meta {

        } bindContract DELETE to { id ->
            {
                perform { remove(id) }
                    .toResponse()
            }
        }
}