package andriesfc.mpt.foundation.functional


sealed interface Either<out E, out R> {

    interface Success<R> : Either<Nothing, R>, Provider<R>

    interface Failure<E> : Either<E, Nothing> {
        fun error(): E
    }

    fun getOrNull(): R? {
        return when (this) {
            is Success<R> -> get()
            else -> null
        }
    }

    fun errorOrNull(): E? {
        return when (this) {
            is Failure<E> -> error()
            else -> null
        }
    }

    data class NonThrowableError(val error: Any?, override val message: String) : RuntimeException(message)
}


inline fun <E, R, T> Either<E, R>.map(mapResult: (R) -> Either<E, T>): Either<E, T> {
    return when (this) {
        is Either.Failure -> this
        is Either.Success -> mapResult(get())
    }
}

fun <E, R> Either<E, R>.get(): R {
    return when (val r = this) {
        is Either.Failure -> throw when (val error = r.error()) {
            is Throwable -> error
            else -> Either.NonThrowableError(error, "$error")
        }

        is Either.Success -> r.get()
    }
}


inline fun <reified E : Throwable, R> responseOf(action: () -> R): Either<E, R> {
    return runCatching(action).response<E, R>()
}

inline fun <reified E, R> responseOf(
    onThrown: (Throwable) -> E?,
    next: () -> R,
) : Either<E,R> {
    return runCatching(next).fold({ it.success() }) { x ->
        if (x is E)
            x.failure()
        else onThrown(x)?.failure() ?: throw x
    }
}

inline fun <reified E : Throwable, R> Result<R>.response(): Either<E, R> {
    return fold({ it.success() }) { x ->
        when (x) {
            is E -> x.failure()
            else -> throw x
        }
    }
}

fun <E> fail(error: E): Either.Failure<E> = error.failure()


fun <E> E.failure(): Either.Failure<E> = FailureImpl(this)
fun <R> R.success(): Either.Success<R> = SuccessImpl(this)


private class SuccessImpl<R>(result: R) : Provider<R> by providerOf(result), Either.Success<R>

private data class FailureImpl<E>(val error: E) : Either.Failure<E> {
    override fun error(): E = error
}
