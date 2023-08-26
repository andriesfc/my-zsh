package andriesfc.powertoys.console

import java.io.PrintWriter
import java.io.Reader
import java.io.Console as JavaIoConsole

class HostConsole private constructor(private val javaIoConsole: JavaIoConsole) : Console {

    companion object {
        private val provided: Console? by lazy { System.console()?.let(::HostConsole) }
        fun console(): Console? = provided
    }

    override fun writer(): PrintWriter = javaIoConsole.writer()
    override fun reader(): Reader = javaIoConsole.reader()
    override fun format(fmt: String, vararg args: Any?): Console = apply { javaIoConsole.format(fmt, args) }
    override fun printf(fmt: String, vararg args: Any?): Console = apply { javaIoConsole.printf(fmt, args) }
    override fun readLine(fmt: String, vararg args: Any?): String = javaIoConsole.readLine(fmt, args)
    override fun readLine(): String = javaIoConsole.readLine()
    override fun readPassword(fmt: String, vararg args: Any?): CharArray = javaIoConsole.readPassword(fmt, args)
    override fun readPassword(): CharArray = javaIoConsole.readPassword()
    override fun flush() = javaIoConsole.flush()

}