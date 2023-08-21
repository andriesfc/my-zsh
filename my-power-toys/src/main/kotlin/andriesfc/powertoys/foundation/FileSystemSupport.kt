package andriesfc.powertoys.foundation

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


fun file(path: String): File {
    return when {
        path.startsWith("~/") -> File(System.getProperty("user.home"), path.tail("~/".length))
        else -> File(path)
    }
}

/**
 * Defensively throws an [IOException] if this files does not exist, or if it does not denote
 * a directory.
 *
 * @throws FileNotFoundException If the file could not be found
 * @throws IOException if [File.path] does not denote a regilar file.
 */
@Throws(FileNotFoundException::class, IOException::class)
fun File.mustExists(): File = apply {

    if (!exists())
        throw FileNotFoundException(path)

    if (!isFile)
        throw IOException("Unexpected regular file here: $path")
}


sealed interface FileName {
    val nameOnly: String
    val extension: String
    val parent: File?
}

operator fun FileName.component1(): String = nameOnly
operator fun FileName.component2(): String = extension
operator fun FileName.component3(): File? = parent

fun File.parts(): FileName = FileNameImpl(this)

@Throws(IOException::class)
fun File.rename(update: FileNameUpdate.() -> Unit): File {
    return when (val dest = FileNameUpdateImpl(this).update(update)) {
        this -> this
        else -> dest.takeIf { renameTo(dest) } ?: throw IOException(buildString {
            append("Unable to rename file \"${this@rename.name}\" to \"${dest.name}\"")
            parent?.also { append(" [").append(parent).append(']') }
        })
    }
}

sealed interface FileNameUpdate : FileName {
    override var extension: String
    override var nameOnly: String
    var fileName: String
    fun reset()
}

private class FileNameImpl(
    val source: File,
    override val nameOnly: String = source.nameWithoutExtension,
    override val extension: String = source.extension,
    override val parent: File? = source.parentFile,
) : FileName {

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is FileNameImpl -> false
            else -> source == other.source
        }
    }

    override fun hashCode(): Int = Objects.hash(source)

    override fun toString(): String = buildString(source.path.length + 4) {
        append(nameOnly)
        if (extension.isNotEmpty()) append('.').append(extension)
        parent?.let {
            append(" [")
            append(parent.path)
            append(']')
        }
    }
}

private class FileNameUpdateImpl(private val source: File) : FileNameUpdate {

    private var _name: String? = null
    private var _ext: String? = null
    override val parent: File? get() = source.parentFile
    private var updateApplied = false

    override var nameOnly: String
        get() = _name ?: source.nameWithoutExtension
        set(value) {
            ensureAvailableForUpdate()
            _name = value.takeUnless { it == source.nameWithoutExtension }
        }

    override var extension: String
        get() = _ext ?: source.extension
        set(value) {
            ensureAvailableForUpdate()
            require(value != ".") { "Value of \".\" is not valid for an extension update." }
            _ext = value.takeUnless { it == source.extension }
        }

    override var fileName: String = buildName(nameOnly, extension)
        set(value) {
            if (value != field) {
                nameOnly = value.substringBefore('.')
                extension = value.substringAfter('.', "")
                field = value
            }
        }

    override fun reset() {
        ensureAvailableForUpdate()
        _name = null
        _ext = null
    }

    private fun buildName(namePart: String, extensionPart: String): String {
        return buildString(namePart.length + extensionPart.length + extensionPart.isEmpty().mapToInt()) {
            append(namePart)
            if (extensionPart.isNotEmpty()) {
                append('.')
                append(extensionPart)
            }
        }
    }

    private fun ensureAvailableForUpdate() {
        updateApplied.requiresValueOf(false) {
            "Update completed already"
        }
    }


    fun update(updateAction: FileNameUpdate.() -> Unit): File {
        ensureAvailableForUpdate()
        updateAction.invoke(this)
        return try {
            when {
                _ext == null && _name == null -> source
                else -> {
                    val updatedName = buildName(nameOnly, extension)
                    parent?.let { File(it, updatedName) } ?: File(parent, updatedName)
                }
            }
        } finally {
            updateApplied = true
        }
    }
}


