package andriesfc.powertoys.foundation

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


fun file(path: String): File {
    return when {
        path.startsWith("~/") -> File(System.getProperty("user.home"), path.tail("~/".length))
        else -> File(path)
    }
}

@Throws(FileNotFoundException::class)
fun File.mustExists(): File = apply {
    if (!exists())
        throw FileNotFoundException(path)
}


@Throws(IOException::class)
fun File.setExtension(ext: String): File {

    if (extension == ext)
        return this

    val update = File(parent, "$nameWithoutExtension.$ext")

    if (!renameTo(update))
        throw IOException("Unable to change extension to $ext for file: $name [in $parent]")

    return update
}

fun File.setName(newName: String): File {

    if (name == newName)
        return this

    val update = File(parent, newName)

    if (!renameTo(update))
        throw IOException("Unable to rename $name to ${update.name} [in $parent]")

    return update
}