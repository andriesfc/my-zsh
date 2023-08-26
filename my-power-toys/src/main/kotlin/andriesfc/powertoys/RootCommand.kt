package andriesfc.powertoys

import andriesfc.powertoys.logging.Level
import io.micronaut.context.annotation.Prototype
import jakarta.inject.Inject
import jakarta.inject.Singleton
import picocli.CommandLine.Command
import picocli.CommandLine.Option


@Command(
    name = "run-power-toy",
    aliases = ["pt"],
    description = ["Runs any of my collection of custom power tools, see help for me detail."],
    version = ["v.0.0.1"],
)
@Singleton
class RootCommand : Runnable {

    @Option(names = ["quiet", "silent", "info", "debug", "error", "trace", "verbose", "v"])
    lateinit var loglevel: Level

    override fun run() {


    }
}


@Prototype
class AllCommands @Inject constructor(@Command private val commands: List<Runnable>) : List<Runnable> by commands