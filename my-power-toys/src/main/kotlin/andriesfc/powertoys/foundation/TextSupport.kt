package andriesfc.powertoys.foundation

fun String.tail(fromIndex: Int): String {
    return when {
        fromIndex >= length -> ""
        else -> substring(fromIndex ..< length)
    }
}