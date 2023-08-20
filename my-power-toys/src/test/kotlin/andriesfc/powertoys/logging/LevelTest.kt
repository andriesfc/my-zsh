package andriesfc.powertoys.logging

import andriesfc.foundation.testing.alsoDebug
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeUnique
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe

class LevelTest : FunSpec({

    test("Levels sorted by priority should be in the correct order (from highest to lowers)") {

        val actualOrder = Level.entries.sortedByDescending(Level::priority)
            .alsoDebug("Actual order")

        val expectedOrder = listOf(Level.Trace, Level.Error, Level.Warn, Level.Debug, Level.Info, Level.Silent)
            .alsoDebug("Expected order")

        actualOrder.shouldContainInOrder(expectedOrder)

    }

    context("Using common names to reference log levels.") {
        test("Supplied common names are not duplicated") {
            val collectedCommonNames = Level.entries
                .flatMap { level -> level.commonNames.map { commonName -> level to commonName } }
                .alsoDebug("All common names")
            collectedCommonNames.shouldBeUnique()
        }
        context("Common level names should always resolve correctly") {
            val expectations = listOf(
                "trace" to Level.Trace,
                "vvv" to Level.Trace,
                "error" to Level.Error,
                "fatal" to Level.Error,
                "warn" to Level.Warn,
                "info" to Level.Info,
                "v" to Level.Info,
                "silent" to Level.Silent,
                "quiet" to Level.Silent,
                "verbose" to Level.Info,
                "debug" to Level.Debug,
                "vv" to Level.Debug
            )
            test("Given expectations covers all possible common names") {
                val actual = expectations.map { (commonName, _) -> commonName }.toSet()
                val expected = Level.entries.flatMap { it.commonNames }.toSet()
                actual.shouldContainAll(expected)
            }
            for ((givenCommonName, expectedLevel) in expectations)
                test(
                    "A common log named \"$givenCommonName\" represents $expectedLevel"
                ) {
                    shouldNotThrowAny { Level.named(givenCommonName) }.shouldBe(expectedLevel)
                }
        }
    }

    context("Resolving effective logging levels") {

        val enforcedVsAllowed: List<Pair<Level, List<Level>>> = listOf(
            Level.Silent to listOf(
                Level.Silent
            ),
            Level.Info to listOf(
                Level.Info, Level.Silent
            ),
            Level.Debug to listOf(
                Level.Debug, Level.Info, Level.Silent
            ),
            Level.Warn to listOf(
                Level.Warn, Level.Debug, Level.Info, Level.Silent
            ),
            Level.Error to listOf(
                Level.Error, Level.Warn, Level.Debug, Level.Info, Level.Silent
            ),
            Level.Trace to listOf(
                Level.Trace, Level.Error, Level.Warn, Level.Debug, Level.Info, Level.Silent
            ),
        ).alsoDebug("Enforced versus allowed")

        val levels = Level.entries

        enforcedVsAllowed.forEach { (enforced, allowToPast) ->
            test("Enforcing log level [${enforced.defaultCommonName}] SHOULD ONLY ALLOW: [${allowToPast.joinToString { it.defaultCommonName }}]") {
                enforced.alsoDebug("Enforced")
                allowToPast.alsoDebug("Allowed to pass")
                val passed = levels.map { Level.enforcedOn(it, enforced) }.toSet().alsoDebug("Actually passed")
                passed.shouldContainOnly(allowToPast)
            }
        }
    }

})
