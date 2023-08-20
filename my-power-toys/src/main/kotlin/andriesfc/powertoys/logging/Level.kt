package andriesfc.powertoys.logging

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

/**
 * Models the level a logger is applicable to.
 *
 * @param priority The priority to establish order independent of the declaration order.
 * @param alternativeCommonNames Some common used alternate names. Note that all alternative names are always lowercase.
 */

@Introspected
enum class Level(val priority: UShort, vararg alternativeCommonNames: String) {

    /**
     * Trace messages the most verbose level of logging, and usually includes all possible messages.
     */
    Trace(5u, "vvv"),

    /**
     * Errors indicate something that a tool could not handle/
     */
    Error(4u, "fatal"),

    /**
     * Warning messages indicate that something unexpected has occurred and alternate flow
     * has been triggered to complete the request.
     */
    Warn(3u),

    /**
     * Debug messages a slightly noisier and normally used to pinpoint problems.
     */
    Debug(2u, "vv"),

    /**
     * Info messages are messages that are intended for user consumption.
     */
    Info(1u, "v", "verbose"),

    /**
     * Silent log instructions are always ignored. As should be treated as a no operation.
     **/
    Silent(0u, "silent", "quiet")
    ;

    /**
     * Common names which consist of the name of the level in lower case plus all specified
     * alternative common names.
     */
    val commonNames = LinkedHashSet<String>(1 + alternativeCommonNames.size).run {
        add(name.lowercase())
        addAll(alternativeCommonNames.map(String::lowercase))
        toList()
    }

    /**
     * First (default) common name
     */
    val defaultCommonName: String get() = commonNames.first()

    companion object {

        private val levelsMappedByCommonNames: Map<String, Level> = entries
            .flatMap { level -> level.commonNames.map { commonName -> commonName to level } }
            .toMap()

        /**
         * Find a level identified by a common name for that level.
         *
         * @param commonName The common name for a specified level.
         * @return The level for that common name.
         *
         * @throws IllegalArgumentException If the common name is not known. The message also includes as list
         * all available common names.
         *
         * @see [Level.commonNames]
         */
        @Creator
        fun named(commonName: String): Level = requireNotNull(levelsMappedByCommonNames[commonName.lowercase()]) {
            "Common level name [$commonName] does not correspond to any level. Note the following " +
                    "names are available: ${levelsMappedByCommonNames.keys.sorted().joinToString()}"
        }

        /**
         * Determines the effective level allowed for an enforced level.
         *
         * @param requested The request level to log to.
         * @param enforced The actual enforced level
         */
        fun enforcedOn(requested: Level, enforced: Level): Level {
            return when {
                requested.priority <= enforced.priority -> requested
                else -> enforced
            }
        }
    }
}