package andriesfc.powertoys.logging

import andriesfc.powertoys.foundation.SystemTime
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.util.Objects

abstract class AbstractLog(
    private var logLevel: Level = Level.Silent,
    private val systemTime: SystemTime,
) : Log {

    override fun appliedLogLevel(): Level = logLevel

    override fun isEnabled(requested: Level): Boolean = Level.enforcedOn(requested, appliedLogLevel()) != Level.Silent

    fun applyLogLevel(level: Level): Level? {
        return when (level) {
            logLevel -> null
            else -> logLevel.also {
                logLevel = level
                configurationChanged()
            }
        }
    }

    override fun log(level: Level, topic: Any, message: String) =
        maybeEmit(level) { createLogMessage(level, topic, null, message) }

    override fun log(level: Level, topic: Any, cause: Throwable, message: String) =
        maybeEmit(level) { createLogMessage(level, topic, cause, message) }

    override fun log(level: Level, topic: Any, lazyMessage: LazyMessage): Boolean {
        return maybeEmit(level) {
            createLogMessage(level, topic, null, lazyMessage())
        }
    }

    override fun log(level: Level, topic: Any, cause: Throwable, lazyMessage: LazyMessage): Boolean {
        return maybeEmit(level) {
            createLogMessage(level, topic, cause, lazyMessage())
        }
    }

    protected open fun configurationChanged() = Unit

    @Introspected
    open class LoggedMessage(
        val timestamp: Instant,
        val level: Level,
        val content: String,
        val cause: Throwable?,
        val topic: Any,
    ) {

        override fun equals(other: Any?): Boolean {
            return when {
                other === this -> true
                other !is LoggedMessage -> false
                else -> timestamp == other.timestamp
                        && level == other.level
                        && content == other.content
                        && cause == other.cause
                        && topic == other.topic
            }
        }

        override fun hashCode(): Int = Objects.hash(timestamp, level, content, cause, topic)
    }

    protected open fun createLogMessage(level: Level, topic: Any, cause: Throwable?, message: String): LoggedMessage =
        LoggedMessage(
            timestamp = systemTime.now(),
            level = level,
            content = message,
            cause = cause,
            topic = topic
        )

    protected abstract fun appendToLogger(message: LoggedMessage)

    private inline fun maybeEmit(level: Level, suppliedMessage: () -> LoggedMessage): Boolean {
        val emitting = Level.enforcedOn(level, logLevel) == Level.Silent
        if (emitting) appendToLogger(suppliedMessage())
        return emitting
    }
}