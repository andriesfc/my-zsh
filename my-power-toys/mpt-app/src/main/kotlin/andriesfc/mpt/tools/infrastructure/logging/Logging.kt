package andriesfc.mpt.tools.infrastructure.logging

import java.time.Instant


interface Logger {


}

interface StructuredPrinter : AutoCloseable {

    interface Level {
        val name: String
        val depth: UShort
    }

    val level: Level?

    fun printf(format: String, vararg args: Any?) = print(format.format(*args))
    fun println()
    fun println(content: String, vararg moreLines: String)
    fun print(content: String)
    fun openLevel()
    fun closeLevel()
}


private const val MIN_PRIORITY = 0

enum class Level(val priority: Int) {

    SILENT(MIN_PRIORITY),
    TRACE(SILENT.priority + 1),
    ERROR(TRACE.priority + 1),
    WARN(ERROR.priority + 1),
    DEBUG(WARN.priority + 1),
    INFO(DEBUG.priority + 1),
    ;

    companion object {

    }
}

sealed class LogMessage<out T : Any> {

    abstract val ts: Instant
    abstract val title: String
    abstract val sensitive: Boolean
    abstract val content: T


}
