package andriesfc.mpt.tools.infrastructure.cli

import andriesfc.mpt.tools.infrastructure.Verbosity
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch

class LoggingOptions : OptionGroup(
    name = "Logging options",
    help = "Controls logging options"
) {

    val verbosity by option(help = "Controls how verbose application emits messages.")
        .switch(*Verbosity.entries.map { it.switch to it }.toTypedArray())
        .default(Verbosity.Silent)

}