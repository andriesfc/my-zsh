package andriesfc.powertoys.console

import andriesfc.powertoys.foundation.HostSystemTime
import andriesfc.powertoys.foundation.SystemTime
import andriesfc.powertoys.logging.AbstractLog
import andriesfc.powertoys.logging.Level
import java.io.PrintStream

/**
 * Logs message record to the console
 */
class ConsoleLog(
    enableAnsi: Boolean,
    systemTime: SystemTime,
    stdout: () -> PrintStream,
) : AbstractLog(Level.Info, systemTime) {

    var enableAnsi: Boolean = enableAnsi; private set

    fun enableAnsi(enabled: Boolean) {
        if (this.enableAnsi != enabled) {
            this.enableAnsi = true
            configurationChanged()
        }
    }

    constructor(enableAnsi: Boolean, systemTime: SystemTime) : this(enableAnsi, systemTime, System::out)
    constructor(systemTime: SystemTime) : this(true, systemTime)
    constructor(): this(HostSystemTime)

    override fun appendToLogger(message: LoggedMessage) {

    }


}