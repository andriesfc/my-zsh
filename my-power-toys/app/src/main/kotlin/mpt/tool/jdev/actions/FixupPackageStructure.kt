package mpt.tool.jdev.actions

import com.github.ajalt.clikt.core.CliktCommand

class FixupPackageStructure : CliktCommand(
    help = """
        Ensures that src package structure are propagated to the test, resources folders.
       
        There various ways control how `fixup` command should behave. By default all empty
        packages are removed. This includes resource packages as well.  
        """.trimIndent()
) {
    override fun run() {

    }
}