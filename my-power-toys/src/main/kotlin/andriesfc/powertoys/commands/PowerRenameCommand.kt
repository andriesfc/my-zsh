package andriesfc.powertoys.commands

import andriesfc.powertoys.commandline.ToolCommand
import andriesfc.powertoys.foundation.file
import andriesfc.powertoys.foundation.mustExists
import andriesfc.powertoys.foundation.setExtension
import andriesfc.powertoys.foundation.setName
import picocli.CommandLine.*

@Command(
    name = "rename",
    aliases = ["rn"],
    description = ["A functional rename command (since unix does not have one)"]
)
@ToolCommand
class PowerRenameCommand : Runnable {

    @Parameters(paramLabel = "Path to file to rename", index = "1")
    lateinit var path: String

    @Parameters(paramLabel = "Update to name")
    lateinit var update: String

    @Option(names = ["--ext"], description = ["Only update the extension and not the whole name"])
    var updateExtension: Boolean = false

    override fun run() {

        val file = file(path).mustExists()

        if ((updateExtension && file.extension == update) || file == file(path)) {
            //log.warn(this) { "Nothing to do: Rename will not have any affect the new name" }
            return
        }

        when {
            updateExtension -> file.setExtension(update)
            else -> file.setName(update)
        }

        //log.info(this) { "Renamed " }
    }
}
