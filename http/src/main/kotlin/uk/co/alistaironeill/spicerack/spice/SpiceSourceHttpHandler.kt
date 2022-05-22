package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.json.JSet
import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Method.*
import org.http4k.lens.Path
import uk.co.alistaironeill.spicerack.error.perform
import uk.co.alistaironeill.spicerack.error.toLens
import uk.co.alistaironeill.spicerack.error.toResponse
import uk.co.alistaironeill.spicerack.http.str
import uk.co.alistaironeill.spicerack.model.JSpice
import uk.co.alistaironeill.spicerack.model.JSpiceUpdate
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.model.SpiceUpdate
import uk.co.alistaironeill.spicerack.source.SpiceSource

object SpiceSourceHttpHandler {
    const val SPICE_PATH = "/api/spice"
    const val SPICE_NAME_PATH = "/api/spiceName"

    private val idPath = Path.str(Spice.Id).of("SpiceId")
    private val namePath = Path.str(Spice.Name).of("SpiceName")
    private val updateLens = JSpiceUpdate.toLens()

    fun SpiceSource.toContractRoutes(): List<ContractRoute> =
        listOf(
            getAll(),
            getById(),
            getByName(),
            create(),
            update(),
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

    private fun SpiceSource.update() =
        SPICE_PATH / idPath meta {

        } bindContract POST to { id ->
            { request ->
                updateLens(request)
                    .perform { update(id, this) }
                    .toResponse()
            }
        }

    private fun SpiceSource.update(id: Spice.Id, update: SpiceUpdate) =
        when (update) {
            is SpiceUpdate.AddAlias -> addAlias(id, update.alias)
            is SpiceUpdate.RemoveAlias -> removeAlias(id, update.alias)
            is SpiceUpdate.Rename -> rename(id, update.name)
            is SpiceUpdate.SetColour -> setColour(id, update.colour)
        }

    private fun SpiceSource.remove() =
        SPICE_PATH / idPath meta {

        } bindContract DELETE to { id ->
            {
                perform { delete(id) }
                    .toResponse()
            }
        }
}