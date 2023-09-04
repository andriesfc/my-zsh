@file:Suppress("MemberVisibilityCanBePrivate")

package andriesfc.mtp.localfs

import andriesfc.mpt.foundation.exception.causedBy
import andriesfc.mpt.foundation.functional.Either
import andriesfc.mpt.foundation.functional.responseOf
import java.io.File
import java.io.IOException
import java.util.*

open class NamingInstruction() {

    private var nameAction: ((target: File) -> File)? = null
    private var nameActionDescription: String? = null

    protected fun setNameAction(actionDescription: String, action: (target: File) -> File) {
        nameAction = action
        nameActionDescription = actionDescription
    }


    override fun toString(): String {
        return buildString {
            append(this@NamingInstruction.javaClass.simpleName)
            append("@").append(this@NamingInstruction.hashCode().toString(16))
            append("{ ")
            nameActionDescription?.also(::append)
            append(" }")
        }
    }

    fun reset(): NamingInstruction {
        nameAction = null
        return this
    }

    fun setName(newNameOnly: String, newExtension: String): NamingInstruction {
        setNameAction("setName($newNameOnly,$newExtension)") { target ->
            File(
                target.parent,
                when {
                    newExtension.isEmpty() -> newExtension
                    else -> "$newNameOnly.$newExtension"
                }
            )
        }
        return this
    }

    fun setNameOnly(newNameOnly: String): NamingInstruction {
        setNameAction("setNameOnly($newNameOnly)") { target ->
            File(
                target.parent,
                when {
                    target.extension.isEmpty() -> newNameOnly
                    else -> "$newNameOnly.${target.extension}"
                }
            )
        }
        return this
    }

    fun setExtensionOnly(newExtension: String): NamingInstruction {
        setNameAction("setExtensionOnly($newExtension)") { target ->
            File(
                target.parentFile,
                "${target.nameWithoutExtension}.$newExtension"
            )
        }
        return this
    }

    fun noExtension(): NamingInstruction {
        setNameAction("RemovesExtension(<AnyFile>)") { target -> File(target.parentFile, target.nameWithoutExtension) }
        return this
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other?.javaClass != javaClass -> false
            else -> {
                (other as NamingInstruction)
                nameActionDescription == other.nameActionDescription && nameAction == other.nameAction
            }
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(nameActionDescription, nameAction)
    }

    protected open fun named(target: File): File = nameAction?.invoke(target) ?: target

    fun ofTarget(target: File): Either<NamingError, File> = responseOf<NamingError, File>(
        { e ->
            if (e is IOException)
                NamingError.UnexpectedRenameFailure(
                    target,
                    message = "Unexpected IO exception occurred while resolving new name of: $target"
                ).causedBy(e)
            else null
        }
    ) { named(target) }
}