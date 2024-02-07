package mpt.tool.jdev

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import mpt.tool.jdev.actions.FixupPackageStructure

class JavaDev : CliktCommand(
    help = """
        A collection of scripts to help with some manual java dev work. For example, to creat a standard project structure. Run the gradle init on etc..
    """.trimIndent(),
    printHelpOnEmptyArgs = true
) {

    init {
        subcommands(
            FixupPackageStructure()
        )
    }

    override fun run() = Unit
}





