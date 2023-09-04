package andriesfc.mpt.foundation

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.math.PI

class MtpFoundationTest : FunSpec({

    test("Producing one line of text from raw text") {
        val raw = """
                Jason is a bloke
            with a whole series of motor coloured
                                        greens in this 
                    house.
            """
        val expectedLine = "Jason is a bloke with a whole series of motor coloured greens in this house."
        raw.line() shouldBe expectedLine
    }

    context("Finding fraction of $PI (PI)") {
        listOf(
            FractionSeekingStrategy.Simple,
            FractionSeekingStrategy.Horners
        ).forEach { using ->
            test("Using strategy - \"$using\"") {
                val fraction = shouldNotThrowAny { PI.fraction(using) }
                println("Fraction of PI: $fraction")
                withClue("Reproducing PI with: [3 + fraction]") {
                    val piAgain = 3 + fraction
                    piAgain shouldBe PI
                }
            }
        }
    }

})

