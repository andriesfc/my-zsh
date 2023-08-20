package andriesfc.powertoys.commandline

import io.micronaut.context.annotation.Prototype
import io.micronaut.context.annotation.Requires
import java.util.concurrent.Callable


@Prototype
@Requires(
    classes = [Runnable::class, Callable::class]
)
annotation class ToolCommand