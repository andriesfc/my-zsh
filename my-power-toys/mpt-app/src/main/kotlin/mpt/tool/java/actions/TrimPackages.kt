package mpt.tool.java.actions

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

class TrimPackages : CliktCommand(
    help = """
        Ensures that there are now empty src packages.
       
        There various ways control how `fixup` command should behave. By default all empty
        packages are removed. This includes resource packages as well.  
        """.trimIndent(),
    name = "trim-packages",
) {

    val emptyPackages by option("--empty-packages")
        .flag(default = true)
        .help("Removes all empty packages from the project.")

    val includeTestFixtures by option("--test-fixtures")
        .flag(default = true)
        .help("Remove empty test fixture packages.")

    val projectDir by option("-p", "--project").file(
        mustExist = true,
        canBeFile = false,
        canBeSymlink = false,
        mustBeWritable = true
    ).defaultLazy(value = { File(".").canonicalFile })
        .help("Sets project folder. Defaults to current working folder.")

    override fun run() {

    }
}