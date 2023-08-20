@file:JvmName("RootCommandLauncher")

package andriesfc.powertoys

import io.micronaut.configuration.picocli.MicronautFactory
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    ApplicationContext.builder(RootCommand::class.java, Environment.CLI).start().use { context ->

        val rootCommand: RootCommand = context.getBean(RootCommand::class.java)
        val subCommands = context.getBean(AllCommands::class.java).filterNot { command -> command == rootCommand }

        val commandLine = CommandLine(rootCommand, MicronautFactory(context))
            .setCaseInsensitiveEnumValuesAllowed(true)
            .setUsageHelpAutoWidth(true)


        subCommands.forEach(commandLine::addSubcommand)

        exitProcess(commandLine.execute(*args))
    }
}





