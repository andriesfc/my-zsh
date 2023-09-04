@file:JvmName("Pedantic")

package andriesfc.mpt.foundation


inline fun illegalStateIf(unexpected: Boolean, lazyMessage: () -> String) {
    if (unexpected)
        throw IllegalStateException(lazyMessage())
}

