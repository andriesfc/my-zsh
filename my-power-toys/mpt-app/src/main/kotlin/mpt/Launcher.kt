@file:JvmName("Launcher")
package mpt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import mpt.framework.emptyRunnable
import mpt.tool.java.JavaTool

private class ToolMain() : Runnable by emptyRunnable(), CliktCommand(
    name = "ptool",
    help = """
        A collection tools and scripts for java/kotlin devs.
    """.trimIndent(),
)

fun main(args: Array<String>) {
    ToolMain()
        .subcommands(JavaTool())
        .main(args)
}
