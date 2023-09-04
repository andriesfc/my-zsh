@file:JvmName("Foundation")

package andriesfc.mpt.foundation

import kotlin.math.floor
import kotlin.math.pow

fun <T> Boolean.case(truth: T, notTrue: T): T = if (this) truth else notTrue

fun Boolean.int():Int = case(1,0)

inline fun <T> eval(evalBlock: () -> T): T = evalBlock()


fun String.line(): String {
    return when {
        isEmpty() -> this
        else -> lineSequence()
            .map(String::trim)
            .filterNot(String::isEmpty)
            .joinToString(" ")
    }
}


/**
 * Strategies for finding fractions of double. Two are supported, a quick method of simply subtraction
 * the whole number part from the number, and 2nd more accurate way called ("Hormers", aka "Horners
 * Method")
 *
 * @see FractionSeekingStrategy.Simple
 * @see FractionSeekingStrategy.Horners
 */
@Suppress("SpellCheckingInspection")
sealed class FractionSeekingStrategy {

    abstract operator fun invoke(number: Double): Double

    /** Simple way, good enough for most cases. */
    data object Simple : FractionSeekingStrategy() {
        override fun invoke(number: Double): Double {
            val integerPart = floor(number)
            return number - integerPart
        }
    }

    /**
     * "Horners Method". Suitable for where fractions become statistically significant. Slower than the
     * [Simple] method.
     */
    data object Horners : FractionSeekingStrategy() {

        override fun invoke(number: Double): Double {
            val fractions = generateSequence(fraction(number)) { f ->
                // sequence of fractions
                when (f) {
                    ZERO -> null // Which stop when we reach aprox ZERO
                    else -> fraction(f * 10.0)
                }
            }.map { f ->
                floor(f * 10.0)        // Extract the next digit in this sequences
            }.withIndex().map { (i, f) ->   // Raise fractions to... 10^-1, 10^-2, etc
                BASE10.pow(-(i + 1)) * f    // so we get .abdcd, .00bdcd, .000bdcd, ...
            }
            return fractions.sum()          // Sum them to get the complete fraction.
        }

        private fun fraction(number: Double) = number - floor(number)
        private const val BASE10: Double = 10.0
        private const val ZERO = 0.0
    }
}

@JvmOverloads
fun Double.fraction(strategy: FractionSeekingStrategy = FractionSeekingStrategy.Simple): Double = strategy(this)


@Suppress("UNCHECKED_CAST")
inline fun <A, B, reified X, reified Y> Pair<A, B>.castOrNull(): Pair<X, Y>? {
    return when {
        first is X && second is Y -> this as Pair<X, Y>
        else -> null
    }
}

fun <T> T.list(): List<T> = listOf(this)
