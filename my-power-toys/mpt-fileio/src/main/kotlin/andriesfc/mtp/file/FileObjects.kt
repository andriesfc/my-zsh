@file:JvmName("FileObjects")

package andriesfc.mtp.file

import java.io.File
import java.util.*

/**
 * User Home Tilde: Denotes part of the path which would normally expanded as the user's home
 * directory.
 */
const val USER_HOME_TILDE = "~"

/** File part separator as denoted by the unix standard. */
const val STANDARD_FILE_PART_SEPARATOR = "/"

val ROOT = File("/")

/** Host File Part Separator. */
inline val HOST_FILE_PART_SEPARATOR: String get() = File.separator

/** User Home (as represented by the `user.home` Java runtime property). */
inline val USER_HOME: File get() = File(System.getProperty("user.home"))

private const val USER_HOME_PATH_EXPANDING = "$USER_HOME_TILDE$STANDARD_FILE_PART_SEPARATOR"

/** Component "1" of a [File] represents the file name without the extension. */
operator fun File.component1(): String = nameWithoutExtension

/** Component "2" of a [File] represents the extension only, or an empty string if not present. */
operator fun File.component2(): String = extension

/** Component "3" of a [File] represents the parent only, (or `null` is none has been set) */
operator fun File.component3(): File? = parentFile

/**
 * Builds a java [File] instance from different parts.
 *
 * @param path The initial path
 * @param remainingParts And remaining parts
 * @param expandHomeTilde Expands the path to the user's home
 * @return A java [File] object correctly constructed from the parts.
 * @throws IllegalArgumentException If expanding of the home-tilde is not permitted, and the path
 *     starts with a **`~/`** sequence.
 */
@Throws(IllegalArgumentException::class)
@JvmOverloads
fun file(path: String, vararg remainingParts: String, expandHomeTilde: Boolean = true): File {

    val parent = when {

        (path == USER_HOME_TILDE || path == USER_HOME_PATH_EXPANDING) ->
            USER_HOME.takeIf { expandHomeTilde }
                ?: throw IllegalArgumentException(
                    "Expanding user home, (via \"$USER_HOME_TILDE\") is not allowed"
                )

        path.startsWith(USER_HOME_PATH_EXPANDING) ->
            USER_HOME.takeIf { expandHomeTilde }?.let {
                File(
                    USER_HOME,
                    path.substringAfter(USER_HOME_TILDE)
                )
            } ?: throw IllegalArgumentException(
                "Expanding user home, (via \"$USER_HOME_TILDE\") is not allowed"
            )

        else ->
            File(path)
    }

    if (remainingParts.isEmpty())
        return parent

    return remainingParts.fold(parent) { currentParent, nextPart -> File(currentParent, nextPart) }
}


/**
 * Standardize a path by replacing all non-standard file separators with the standard unix file
 * separator (`/`).
 *
 * @param hostFileSeparator The host's current file separator
 * @param pathString The path string
 * @return A standard path which only uses `/` as part separator
 * @see HOST_FILE_PART_SEPARATOR
 * @see STANDARD_FILE_PART_SEPARATOR
 */
@JvmOverloads
fun standardizePath(hostFileSeparator: String = HOST_FILE_PART_SEPARATOR, pathString: String): String {
    return when {
        pathString.isEmpty() -> pathString
        hostFileSeparator == STANDARD_FILE_PART_SEPARATOR -> pathString
        else -> pathString.splitToSequence(hostFileSeparator).joinToString(STANDARD_FILE_PART_SEPARATOR)
    }
}

/**
 * Potentially retrieve all ancestors from a given Java [File] object.
 *
 * Specifically, calling `file("/usr/lib/bin/x/1/data.mdb").ancestors().toList()` produces the
 * following (in the exact order):
 *
 * 1. `File("/usr/lib/bin/x/1")`
 * 2. `File("/usr/lib/bin/x")`
 * 3. `File("/usr/lib/bin"`
 * 4. `File("/usr/lib")`
 * 5. `File("/usr")`
 * 6. `File("/")`
 *
 * @return A sequence of ancestor files.
 */
fun File.ancestors(): Sequence<File> {
    var next = parentFile
    return generateSequence {
        val ancestor = next
        next = ancestor?.parentFile
        ancestor
    }
}

/**
 * Breaks a Java [File] object in parts. For example, the `file("/usr/lib/bin/x/1/data.mdb")` produces
 * a list with the following parts:
 * 1. `"/"`
 * 2. `"usr"`
 * 3. `"lib"`
 * 4. `"bin"`
 * 5. `"x"`
 * 6. `"1"`
 * 7. `"data.mdb"`
 *
 * @return
 */
fun File.parts(): List<String> {
    return LinkedList<String>().run {
        var current: File? = this@parts
        while (true) {
            when (current) {
                null -> break
                ROOT -> addFirst("/")
                else -> addFirst(current.name)
            }
            current = current.parentFile
        }
        toList()
    }
}

inline val File.isRoot: Boolean get() = this == ROOT

fun File.isNotPresent():Boolean = !exists()
fun File.isNotFile(): Boolean = !isFile