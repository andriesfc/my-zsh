import andriesfc.powertoys.console.AnsiConsole
import andriesfc.powertoys.console.Console
import andriesfc.powertoys.foundation.SystemTime
import andriesfc.powertoys.logging.AbstractLog
import andriesfc.powertoys.logging.Level


/**
 * Console log prints out styled messages to the console. Note that actual style for
 * [AbstractLog.LoggedMessage] are handled by the [styler].
 *
 * @param systemTime Gives the console access to application-controlled system time.
 * @constructor Constructs a new logger given the supplied configuration parameters.
 * @property console Console to which print out styled user-friendly messages.
 * @property styler The object which is responsible to generate styled messages.
 * @property ansiEnabled Enabled ANSI output (only if the styler supports ANSI). This set by default to
 *     `true` if the [console] is also an [AnsiConsole]).
 * @property flushAfterAppend Flush each message after append to the console (true by default).
 */
class ConsoleLog(
    systemTime: SystemTime,
    private val console: Console,
    private val styler: MessageStyler,
    private val ansiEnabled: Boolean = console is AnsiConsole,
    private val flushAfterAppend: Boolean = true,
) : AbstractLog(Level.Info, systemTime) {

    override fun appendToLogger(message: LoggedMessage) {
        val styledMessage = StringBuilder().run {
            if (ansiEnabled && styler is AnsiCapableMessageStyler)
                styler.buildAnsiStyled(this, message)
            else styler.buildStyled(this, message)
            toString()
        }
        appendStyled(styledMessage)
    }

    private fun appendStyled(styledMessage: String) = with(console) {
        printf("%s", styledMessage)
        if (flushAfterAppend) flush()
    }

    /**
     * Message styler is responsible to create human friendly `styled` message which are then printed to
     * the console.
     *
     * @constructor Create empty Message styler
     * @see [Console.printf]
     */
    fun interface MessageStyler {
        /**
         * Build styled message based on what was logged via [logged] message.
         *
         * @param styledMessage Buffer which the styler may use to build the styled messages.
         * @param logged The logged message.
         */
        fun buildStyled(styledMessage: StringBuilder, logged: LoggedMessage)
    }

    /**
     * Ansi capable message styler
     *
     * @constructor Create empty Ansi capable message styler
     */
    interface AnsiCapableMessageStyler : MessageStyler {
        /**
         * Build ansi styled
         *
         * @param styledMessage
         * @param logged
         */
        fun buildAnsiStyled(styledMessage: StringBuilder, logged: LoggedMessage)
    }
}
