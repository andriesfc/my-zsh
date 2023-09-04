package andriesfc.mpt.foundation.functional

import java.util.*


interface Provider<out T> {
    fun get(): T
    fun <R> map(map: (T) -> R): Provider<R>
    operator fun invoke(): T = get()
}

private sealed class ValueProvider<T> : Provider<T> {

    class Value<T>(private val value: T) : ValueProvider<T>() {
        override fun get(): T = value
        override fun <R> map(map: (T) -> R): Provider<R> {
            return Supplier { map(get()) }
        }

        override fun equals(other: Any?): Boolean {
            return when {
                other === this -> true
                other !is Value<*> -> false
                else -> value == other.value
            }
        }

        override fun hashCode(): Int = Objects.hashCode(value)

        override fun toString(): String {
            return buildString {
                append(Value::class.java.simpleName)
                append("={").append(value).append("}")
            }
        }
    }

    class Supplier<T>(private val supplier: () -> T) : ValueProvider<T>() {

        override fun get(): T = supplier.invoke()

        override fun <R> map(map: (T) -> R): Provider<R> = Supplier { map(get()) }

        override fun toString(): String {
            return buildString {
                append(Supplier::class.java.simpleName)
                append("with {").append(supplier).append("}")
            }
        }

        override fun hashCode(): Int = Objects.hashCode(supplier)

        override fun equals(other: Any?): Boolean {
            return when {
                other === this -> true
                other !is Supplier<*> -> false
                else -> supplier == other.supplier
            }
        }
    }
}


fun <T> providerOf(value: T): Provider<T> = ValueProvider.Value(value)
fun <T> providerOf(supplier: () -> T): Provider<T> = ValueProvider.Supplier(supplier)
