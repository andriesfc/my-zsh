package andriesfc.foundation.testing

fun <T> T.alsoDebug(label: String): T {

    if (label.isNotBlank()) {
        print(label)
        print(": ")
    }

    println(this)
    return this
}

