package andriesfc.powertoys.commandline

import andriesfc.powertoys.foundation.uniqueList
import io.micronaut.context.annotation.Prototype
import io.micronaut.context.annotation.Requires
import picocli.CommandLine.ITypeConverter
import java.util.concurrent.Callable


@Prototype
@Requires(
    classes = [Runnable::class, Callable::class]
)
annotation class ToolCommand

fun interface CommonNameProvider {

    fun commonNames(): List<String>

}

fun commonNamesProvider(first: String, vararg more: String): CommonNameProvider {
    val commonNames = uniqueList(first, *more)
    return CommonNameProvider { commonNames }
}

abstract class EnumCommonNamedConverter<E>(enumClass: Class<E>, private val use: String) : ITypeConverter<E>
        where E : CommonNameProvider,
              E : Enum<E> {

    private val mappedCommonNames =
        enumClass.enumConstants.flatMap { e -> e.commonNames().map { cn -> cn to e } }.toMap()

    override fun convert(value: String?): E {
        return requireNotNull(mappedCommonNames[value]) {
            "Unable to convert common name value of \"$value\" for use \"$use\". Note the following values are available : [${
                mappedCommonNames.keys.joinToString()
            }]"
        }
    }
}