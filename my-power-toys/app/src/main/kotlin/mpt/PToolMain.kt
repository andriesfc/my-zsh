package mpt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import mpt.tool.jdev.JavaDev

private class PToolMain() : CliktCommand(
    name = "ptool",
    help = """
        A collection tools and scripts for java/kotlin devs.
    """.trimIndent(),
) {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    PToolMain()
        .subcommands( JavaDev() )
        .main(args)
}
