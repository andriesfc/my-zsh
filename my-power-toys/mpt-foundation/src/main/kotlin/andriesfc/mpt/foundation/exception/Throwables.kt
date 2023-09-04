package andriesfc.mpt.foundation.exception

fun <X : Throwable> X.causedBy(e: Throwable): X = apply {
    require(e != this)
    initCause(e)
}

fun Throwable.causes(): Sequence<Throwable> = generateSequence(cause) { e -> e.cause }

fun Throwable.rootCause(): Throwable? = causes().lastOrNull()
