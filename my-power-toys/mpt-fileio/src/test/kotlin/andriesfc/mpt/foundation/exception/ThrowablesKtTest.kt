package andriesfc.mpt.foundation.exception

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import java.io.IOException
import java.net.SocketTimeoutException

class ThrowablesKtTest : FunSpec({

    test("Set cause and return the actual type being set") {

        val runtimeExceptionCauseByIO: RuntimeException = RuntimeException("runtime exception")
            .causedBy(IOException("cannot read"))

        runtimeExceptionCauseByIO.shouldBeInstanceOf<RuntimeException>()
        runtimeExceptionCauseByIO.cause.shouldBeInstanceOf<IOException>()
    }

    context("Simple stack tracing") {

        val rootCause = IllegalStateException("io-1-illegal-state")
        val initialCauseCasedBy = SocketTimeoutException("io-1-timed-out").causedBy(rootCause)
        val initialCause = IOException("io-1").causedBy(initialCauseCasedBy)
        val thrown = IOException("io-1-cannot-read").causedBy(initialCause)

        val causes = thrown.causes().toList()

        test("Correctly trace all causes") {
            assertSoftly(causes) {
                withClue("\nThere should be 3 causes:") { shouldHaveSize(3) }
                withClue("\nCorrect root cause: ${rootCause.message}") { last() shouldBeSameInstanceAs rootCause }
                withClue("\nTrace:") {
                    causes.shouldBe(
                        listOf(
                            initialCause,
                            initialCauseCasedBy,
                            rootCause
                        )
                    )
                }
            }
        }

        test("Reports root cause") {
            thrown.rootCause() shouldBeSameInstanceAs rootCause
        }
    }


})
