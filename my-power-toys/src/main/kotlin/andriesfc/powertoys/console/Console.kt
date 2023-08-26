package andriesfc.powertoys.console

import java.io.PrintWriter
import java.io.Reader

interface Console {
    fun writer(): PrintWriter
    fun reader(): Reader
    fun format(fmt: String, vararg args: Any?): Console
    fun printf(fmt: String, vararg args: Any?): Console
    fun readLine(fmt: String, vararg args: Any?): String
    fun readLine(): String
    fun readPassword(fmt: String, vararg args: Any?): CharArray
    fun readPassword(): CharArray
    fun flush()
}

