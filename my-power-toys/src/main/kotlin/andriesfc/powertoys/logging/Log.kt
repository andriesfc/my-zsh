package andriesfc.powertoys.logging

internal typealias LazyMessage = () -> String

interface Log {
    fun appliedLogLevel(): Level
    fun log(level: Level, topic: Any, cause: Throwable, message: String): Boolean
    fun log(level: Level, topic: Any, cause: Throwable, lazyMessage: LazyMessage): Boolean
    fun log(level: Level, topic: Any, message: String): Boolean
    fun log(level: Level, topic: Any, lazyMessage: LazyMessage): Boolean
    fun isEnabled(requested: Level): Boolean
}

