package andriesfc.mtp.localfs

import andriesfc.mpt.foundation.functional.get
import andriesfc.mtp.file.file
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.File
import java.security.MessageDigest
import java.util.*

class FileNamingTest : FunSpec({
    val dir = tempdir()
    context("Test Failure cases") {

        val naming = NamingInstruction()

        afterTest { naming.reset() }

        lateinit var target: File
        beforeTest {
            target = file(dir.path, "target-${UUID.randomUUID()}")
        }

        test("Attempting to rename a non existing file should report with FileNamingError.TargetNotFound") {
            assertSoftly(target.rename(naming.setExtensionOnly("pr1"))) {
                shouldThrow<NamingError.TargetNotFound> { get() }
                errorOrNull().shouldNotBeNull().shouldBeInstanceOf<NamingError.TargetNotFound>()
            }
        }
        test("Attempting to rename a non regular file should fail with a FileNamingError.TargetNotRegularFile") {
            target.mkdir().shouldBeTrue()
            naming.setExtensionOnly("dr1")
            assertSoftly(target.rename(naming)) {
                shouldThrow<NamingError.TargetNotRegularFile> { get() }
                errorOrNull().shouldNotBeNull().shouldBeInstanceOf<NamingError.TargetNotRegularFile>()
            }
        }
        test("Attempting to rename a file to the the name of an existing file should fail with a FileNamingError.DestinationFileExistsAlready") {
            target.writeText("hello1")
            val destName = "data"
            val destExtension = "txt"
            File(dir, "$destName.$destExtension").writeText("data")
            naming.setName(destName, destExtension)
            assertSoftly(target.rename(naming)) {
                shouldThrow<NamingError.DestinationExistsAlready> { get() }
                errorOrNull().shouldNotBeNull().shouldBeInstanceOf<NamingError.DestinationExistsAlready>()
            }
        }
        test("Renaming a to file pointing to another file should fail with FileRenameError.DestinationParentNotTarget") {
            target.writeText("1")
            naming.setName("/1/2/3/data", "txt")
            assertSoftly(target.rename(naming)) {
                shouldThrow<NamingError.DestinationParentNotTarget> { get() }
                errorOrNull().shouldNotBeNull().shouldBeInstanceOf<NamingError.DestinationParentNotTarget>()
            }
        }
    }

    context("Edge cases") {
        test("Renaming a target to itself is an NO-OP") {
            val target = File(dir, "self.ok1").apply { writeBytes(byteArrayOf()) }
            val naming = NamingInstruction().setName(target.nameWithoutExtension, target.extension)
            val r = shouldNotThrowAny { target.rename(naming) }
            assertSoftly {
                r.errorOrNull().shouldBeNull()
                r.get() shouldBeEqual target
            }
        }
    }

    context("Renaming File & file parts") {

        val naming = NamingInstruction()
        lateinit var target: File
        lateinit var dest: File

        beforeTest {
            target = File(dir, "testdata-${it.id()}.txt")
            target.writeText(it.name.testName)
            naming.reset()
        }

        afterTest { (_, r) ->
            if (r.isSuccess) {
                println("Renamed : ${target.name}\n\t\tTO: ${dest.name}")
            }
        }

        test("Rename complete name") {
            val x = File(dir, "newNameOnly.new1")
            dest = target.rename(naming.setName(x.nameWithoutExtension, x.extension)).get()
            dest shouldBeEqual x
            dest.shouldExist()
        }

        test("Rename just the extension") {
            val x = File(dir, "${target.nameWithoutExtension}.gj9")
            dest = target.rename(naming.setExtensionOnly(x.extension)).get()
            dest shouldBe x
            dest.shouldExist()
        }
        test("Rename just the name, but not the extension") {
            val x = File(dir, "one-pun-dro.${target.extension}")
            dest = target.rename(naming.setNameOnly(x.nameWithoutExtension)).get()
            dest shouldBe x
            dest.shouldExist()
        }
        test("Just remove the extension") {
            val x = File(dir, target.nameWithoutExtension)
            dest = target.rename(naming.noExtension()).get()
            dest shouldBe x
        }
    }
})

private fun TestCase.id(): String = MessageDigest
    .getInstance("sha1")
    .digest(name.testName.encodeToByteArray())
    .run(HexFormat.of()::formatHex)
