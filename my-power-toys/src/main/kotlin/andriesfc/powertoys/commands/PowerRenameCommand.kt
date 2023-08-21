package andriesfc.powertoys.commands

import andriesfc.powertoys.commandline.CommonNameProvider
import andriesfc.powertoys.commandline.EnumCommonNamedConverter
import andriesfc.powertoys.commandline.ToolCommand
import andriesfc.powertoys.commandline.commonNamesProvider
import andriesfc.powertoys.foundation.file
import andriesfc.powertoys.foundation.mustExists
import andriesfc.powertoys.foundation.rename
import picocli.CommandLine.*

@Command(
    name = "rename",
    aliases = ["rn"],
    description = ["A functional rename command (since unix does not have one)"]
)
@ToolCommand
class PowerRenameCommand : Runnable {

    enum class RenameTarget(commonName: String, vararg alternateCommonNames: String) :
        CommonNameProvider by commonNamesProvider(commonName, *alternateCommonNames) {

        Extension("ext"),
        NameOnly("name-only"),
        FullName("name");

        internal object Converter : EnumCommonNamedConverter<RenameTarget>(
            RenameTarget::class.java,
            "rename target"
        )
    }

    @Option(
        description = ["Determine which part of the file name should be targeted."],
        names = ["-t", "--target"],
        defaultValue = "ext",
        showDefaultValue = Help.Visibility.ALWAYS,
        converter = [RenameTarget.Converter::class]
    )
    lateinit var updateTarget: RenameTarget

    @Option(
        description = ["Resolved file to renamed to an absolute value."],
        names = ["--absolute", "-A"],
        defaultValue = "false",
        showDefaultValue = Help.Visibility.ALWAYS
    )
    var resolveAsAbsolute: Boolean = false

    @Option(
        description = [
            "Resolves the file to a canonical representation, before attempting",
            "to rename."],
        names = ["--canonical", "-C"],
        showDefaultValue = Help.Visibility.ALWAYS
    )
    var resolveCanonical: Boolean = false

    @Parameters(
        paramLabel = "PATH",
        index = "1",
        description = [
            "The file to rename"
        ]
    )
    lateinit var path: String

    @Parameters(paramLabel = "NEW_NAME")
    lateinit var update: String


    override fun run() {
        val file = file(path)
            .mustExists()
            .run { if (resolveCanonical) canonicalFile else this }
            .run { if (resolveAsAbsolute) absoluteFile else this }
            .rename {
                when (updateTarget) {
                    RenameTarget.Extension -> extension = update
                    RenameTarget.NameOnly -> nameOnly = update
                    RenameTarget.FullName -> fileName = update
                }
            }
    }
}

