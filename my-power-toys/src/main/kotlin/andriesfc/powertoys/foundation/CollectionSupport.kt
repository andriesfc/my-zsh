package andriesfc.powertoys.foundation

import java.util.LinkedHashSet

fun <T> uniqueList():List<T> = emptyList()

fun <T> uniqueList(first:T, vararg more:T): List<T> {
    return when {
        more.isEmpty() -> listOf(first)
        else -> LinkedHashSet<T>().run {
            add(first)
            more.forEach(::add)
            toList()
        }
    }
}
