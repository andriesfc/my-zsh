package andriesfc.mtp.localfs

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * File rename error. These errors can both be treated as Data, and as throwable [IOException]s.
 *
 * @param message The message
 */
sealed class NamingError(message: String) : IOException(message) {

    abstract val target: File

    /**
     * Target file to rename could not be found
     *
     * @constructor Create empty Target not found
     * @property target The target [File] object
     * @property cause The cause (which defaults to [FileNotFoundException]
     */
    data class TargetNotFound(
        override val target: File,
        override val cause: FileNotFoundException = FileNotFoundException(target.path),
    ) : NamingError("target not found: ${cause.message}")

    /**
     * Target file is not a regular file.
     *
     * @constructor Create empty Target not regular file
     * @property target The file being renamed
     */
    data class TargetNotRegularFile(override val target: File) :
        NamingError("Expected regular file here: ${target.path}")

    /**
     * Destination exists already error indicates that the rename instruction attempted to
     * name an existing target to file which exists already.
     *
     * @constructor Create empty Destination exists already
     * @property target The file being renamed
     * @property destination The destination(file being renamed)
     */
    data class DestinationExistsAlready(override val target: File, val destination: File) :
        NamingError("Unable to rename  $target to ${destination.name}: Destination exists already.")

    /**
     * Destination's parent is not the same as the target. Renaming an existing target does not make sense,
     * and will always fail. Need copy, or move in this case.
     *
     * @constructor Create empty Destination parent not target
     * @property instruction
     * @property target
     */
    data class DestinationParentNotTarget(
        val instruction: NamingInstruction,
        override val target: File,
    ) : NamingError("Unable to rename ${target.name}. Instruction not targeting the same parent [$instruction].")

    /**
     * Unexpected rename failure
     *
     * @constructor Create empty Unexpected rename failure
     * @property target File being renamed
     * @property dest The destination (new) file name.
     * @property message A useless message. Java does not really indicate why a [File.renameTo] fails.
     */
    data class UnexpectedRenameFailure(
        override val target: File,
        val dest: String? = null,
        override val message: String = "Something unexpected happened. Could not rename $target to $dest",
    ) : NamingError(message)
}