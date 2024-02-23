package mpt.tool.java

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import mpt.framework.emptyRunnable
import mpt.tool.java.actions.TrimPackages

class JavaTool : Runnable by emptyRunnable(), CliktCommand(
    name = "java",
    help = """
        A collection of scripts to help with some manual java dev work. 
        
    """.trimIndent(),
    printHelpOnEmptyArgs = true
) {
    init {
        subcommands(
            TrimPackages()
        )
    }
}





