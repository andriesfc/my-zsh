package andriesfc.mpt.tools.infrastructure.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.groups.provideDelegate

abstract class Tool(
    toolCommandName: String,
    toolHelp: String,
    toolEpilog: String = "",
) : CliktCommand(
    printHelpOnEmptyArgs = true,
    help = toolHelp,
    epilog = toolEpilog,
    name = toolCommandName
) {

    init {
        context {
            helpFormatter = { context ->
                MordantHelpFormatter(
                    context,
                    showDefaultValues = true,
                    requiredOptionMarker = "*"
                )
            }
        }
    }

    override fun run() = Unit

    val loggingOptions by LoggingOptions()

}


const val NEL = "\u0085"
