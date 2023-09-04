package andriesfc.mtp.localfs

import andriesfc.mpt.foundation.functional.Either
import andriesfc.mpt.foundation.functional.fail
import andriesfc.mpt.foundation.functional.map
import andriesfc.mpt.foundation.functional.success
import andriesfc.mtp.file.isNotFile
import andriesfc.mtp.file.isNotPresent
import java.io.File

/**
 * Rename an existing file based on a [NamingInstruction] passed to it.
 *
 * @param naming
 * @return Either the renamed file, or a [NamingError] indicating why the rename operation could not
 *     proceed
 */
fun File.rename(naming: NamingInstruction): Either<NamingError, File> {
    return if (isNotPresent())
        fail(NamingError.TargetNotFound(this))
    else if (isNotFile())
        fail(NamingError.TargetNotRegularFile(this))
    else naming.ofTarget(this).map { dest ->
        if (dest == this)
            this.success()
        else if (dest.exists())
            fail(NamingError.DestinationExistsAlready(this, dest))
        else if (dest.parent != parent)
            fail(NamingError.DestinationParentNotTarget(naming, this))
        else if (!renameTo(dest))
            fail(NamingError.UnexpectedRenameFailure(this, dest.name))
        else dest.success()
    }
}

