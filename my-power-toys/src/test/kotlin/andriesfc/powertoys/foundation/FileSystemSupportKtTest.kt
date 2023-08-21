package andriesfc.powertoys.foundation

import andriesfc.foundation.testing.alsoDebug
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldHaveFileSize
import io.kotest.matchers.file.shouldHaveName
import io.kotest.matchers.file.shouldHaveNameWithoutExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldNotBeEmpty
import java.io.File
import java.util.*
import kotlin.random.Random
import kotlin.random.nextLong

class FileSystemSupportKtTest : FunSpec({
    context("Destructuring file parts") {
        test("File with no extension, and no parent") {
            val (name, ext, parent) = File("data").parts()
            name shouldBe "data"
            ext.shouldBeEmpty()
            parent shouldBe null
        }
        test("File with no extension and parent") {
            val (name, ext, parent) = File("path/to/some/data").parts()
            name shouldBe "data"
            ext shouldBe ""
            parent?.path shouldBe "path/to/some"
        }
        test("File with parent, name and extension") {
            val (name, ext, parent) = File("/path/to/some/data.txt").parts()
            name shouldBe "data"
            ext shouldBe "txt"
            parent?.path shouldBe "/path/to/some"
        }
    }
    context("Renaming using file parts") {

        val workDir = tempdir()
        var expectedFileSize: Long = 0
        lateinit var dataFile: File

        beforeTest {
            expectedFileSize = Random.nextLong(300L..<600L).alsoDebug("Expected file size (bytes)")
            dataFile = File(workDir, "${UUID.randomUUID()}.data").alsoDebug("Test Data")
            dataFile.writeBytes(Random.nextBytes(expectedFileSize.toInt()))
            dataFile.length().alsoDebug("Actual file size (bytes)")
        }

        test("Changing no perts in a file should result the exact same file") {
            val actualFile = dataFile.rename { }
            actualFile shouldBeEqual dataFile
        }
        test("Changing the part of a file ") {
            val actualFile = dataFile.rename {
                nameOnly = dataFile.nameWithoutExtension
                extension = dataFile.extension
            }
            actualFile shouldBeEqual dataFile
        }
        test("Renaming only the extension") {
            val expectedExtension = "0d1"
            val expectedFileName = dataFile.nameWithoutExtension
            val actualFile = dataFile.rename { extension = expectedExtension }
            actualFile.shouldExist()
            actualFile.shouldHaveFileSize(expectedFileSize)
            actualFile.extension.shouldBe(expectedExtension)
            actualFile.nameWithoutExtension.shouldBe(expectedFileName)
        }
        test("Renaming onl the file name part without the extension") {
            val expectedFileNameOnly = "brian-key-data"
            val expectedFileExtension = dataFile.extension
            val actualFile = dataFile.rename { nameOnly = expectedFileNameOnly }
            actualFile.shouldExist()
            actualFile.shouldHaveFileSize(expectedFileSize)
            actualFile.extension.shouldBe(expectedFileExtension)
            actualFile.nameWithoutExtension.shouldBe(expectedFileNameOnly)
        }
        test("Renaming filename with extension in one go") {
            val expectedFileName = "daddyBlueJack"
            val actualFile = dataFile.rename { fileName = expectedFileName }.alsoDebug("Actual file (after rename)")
            actualFile shouldHaveName expectedFileName
            actualFile shouldHaveNameWithoutExtension expectedFileName
        }
        test("Rename by removing extension") {
            val expectedFileNameOnly = dataFile.nameWithoutExtension
            val actualFile = dataFile.rename { extension = "" }.alsoDebug("Actual file (after rename)")
            actualFile.shouldHaveNameWithoutExtension(expectedFileNameOnly)
        }
        context("Enforcing invalid input") {
            test("Using an extension with just a dot should raise an IllegalArgument exception") {
                val raisedException =
                    shouldThrow<IllegalArgumentException> { dataFile.rename { extension = "." } }
                        .alsoDebug("Expecting")
                raisedException.message.shouldNotBeEmpty()
            }
        }
    }
})


