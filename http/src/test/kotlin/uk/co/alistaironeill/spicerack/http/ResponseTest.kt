package uk.co.alistaironeill.spicerack.http

import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import com.ubertob.kondor.json.num
import com.ubertob.kondor.json.str
import com.ubertob.kondor.outcome.asFailure
import com.ubertob.kondor.outcome.asSuccess
import org.http4k.core.Response
import org.http4k.core.Status.Companion.I_M_A_TEAPOT
import org.junit.jupiter.api.Test
import strikt.assertions.contains
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.error.ActionError
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.error.UnexpectedError
import uk.co.alistaironeill.spicerack.error.toResponse
import uk.co.alistaironeill.spicerack.outcome.expectFailure
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

class ResponseTest {
    private data class Foo(val foo: String, val bar: Int)
    private object JFoo: JAny<Foo>() {
        private val foo by str(Foo::foo)
        private val bar by num(Foo::bar)
        override fun JsonNodeObject.deserializeOrThrow() = Foo(+foo, +bar)
    }

    @Test
    fun `can successfully return a foo by http`() {
        Foo("foo", 2)
            .asSuccess()
            .toResponse(JFoo)
            .handle(JFoo)
            .expectSuccess()
            .and {
                get { foo }.isEqualTo("foo")
                get { bar }.isEqualTo(2)
            }
    }

    @Test
    fun `can successfully get a unit outcome by http`() {
        Unit.asSuccess()
            .toResponse()
            .handle()
            .expectSuccess()
    }

    @Test
    fun `can successfully get an error from typed parsing via http`() {
        NotFound("Foo", "foo")
            .let(::ActionError)
            .asFailure()
            .toResponse(JFoo)
            .handle(JFoo)
            .expectFailure()
            .isA<NotFound.Simple>()
            .and {
                get { type }.isEqualTo("Foo")
                get { id }.isEqualTo("foo")
            }
    }

    @Test
    fun `can successfully get an error from unit via http`() {
        NotFound("Foo", "foo")
            .let(::ActionError)
            .asFailure()
            .toResponse()
            .handle()
            .expectFailure()
            .isA<NotFound.Simple>()
            .and {
                get { type }.isEqualTo("Foo")
                get { id }.isEqualTo("foo")
            }
    }

    @Test
    fun `returns an unexpected error when response is unparsable`() {
        Response(I_M_A_TEAPOT)
            .body("foo")
            .handle(JFoo)
            .expectFailure()
            .isA<UnexpectedError>()
            .and {
                get { code }.isEqualTo(418)
                get { message }.contains("Invalid Json")
            }
    }
}