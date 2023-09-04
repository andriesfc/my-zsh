@file:Suppress("IdentifierGrammar")

package andriesfc.mtp.file

import io.kotest.assertions.asClue
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldNotEndWith
import java.io.File

class FileObjectsTest : FunSpec({
    context("Destructuring file parts") {
        test("File with no parent should return \"null\" as a parent") {
            val fileWithNoParent = File("report-1.txt")
            val (_, _, parent) = fileWithNoParent
            fileWithNoParent.asClue { parent.shouldBeNull() }
        }
        test("Destructing path which ends in a standard file path separator ($STANDARD_FILE_PART_SEPARATOR)") {
            val p = File("/jason/is/here/now/")
            println(p)
            val (name, ext, parent) = p
            assertSoftly {
                p.path.shouldNotEndWith(STANDARD_FILE_PART_SEPARATOR)
                parent?.path shouldBe "/jason/is/here"
                name shouldBeEqual "now"
                ext.shouldBeEmpty()
            }
        }
        test("Destructuring normal file with extension") {
            val (name, ext, parent) = File("/usr/lib/oreo/data.txt")
            assertSoftly {
                parent?.path?.shouldBeEqual("/usr/lib/oreo")
                name shouldBeEqual "data"
                ext shouldBeEqual "txt"
            }
        }
        test("Destructuring normal file without extensions ") {
            val (name, ext, parent) = File("/usr/lib/oreo/data")
            assertSoftly {
                parent?.path?.shouldBeEqual("/usr/lib/oreo")
                name shouldBeEqual "data"
                ext shouldBeEqual ""
            }
        }
    }
    context("Standardize path strings") {
        context("On windows operating system should standardize on using the  standard file part separator ($STANDARD_FILE_PART_SEPARATOR)") {
            val windowsSeparator = "\\"
            val windowsPathString = "\\john\\dest\\ope\\blah"
            val expected = "/john/dest/ope/blah"
            val actual = standardizePath(windowsSeparator, windowsPathString)
            expected shouldBeEqual actual
        }
        context("On standard unix type operating system") {
            val unixSeparator = "/"
            val unixPath = "/usr/lib/is/here"
            val expected = "/usr/lib/is/here"
            val actual = standardizePath(unixSeparator, unixPath)
            actual shouldBeEqual expected
        }
    }
    context("Building path from parts") {
        test("Just passing a single value") {
            val expected = File("data.txt")
            val actual = file("data.txt")
            actual shouldBeEqual expected
        }
        test("Passing multiple values") {
            val expected = File("/usr/lib/growling.so")
            val actual = file("/", "usr", "lib", "growling.so")
            actual shouldBeEqual expected
        }
        test("The file ~/projects/bilbo should expand to: $USER_HOME/projects/bilbo") {
            val expected = File(System.getProperty("user.home"), "projects/bilbo")
            withClue("Expecting: $expected") {
                val actual = file("~", "projects", "bilbo")
                "actual: $actual".asClue {
                    actual.path shouldBeEqual expected.path
                }
            }
        }
        test("Building path with a home tilde should throw an illegal argument if this feature is turned off") {
            shouldThrow<IllegalArgumentException> {
                file("~", ".local", "h2", "databases", expandHomeTilde = false)
            }
        }
        test("The path consisting of \"~\" should resolve to current user's home ($USER_HOME)") {
            file("~") shouldBeEqual USER_HOME
        }
        test("The path consisting of \"~/\" should resolve to the current user's home ($USER_HOME)") {
            file("~/") shouldBeEqual USER_HOME
        }
    }
    context("Working file parts as in relation to others") {
        test("Finding ancestors of file") {
            val fileObj = file("/usr/lib/bin/x/1/data.mdb")
            fileObj.ancestors().toList().shouldBeEqual(
                listOf(
                    file("/usr/lib/bin/x/1"),
                    file("/usr/lib/bin/x"),
                    file("/usr/lib/bin"),
                    file("/usr/lib"),
                    file("/usr"),
                    file("/"),
                )
            )
        }
        test("Breaking a file into parts") {
            val fileObj = file("/usr/lib/bin/x/1/data.mdb")
            val parts = fileObj.parts()
            parts.shouldBeEqual(
                listOf(
                    "/",
                    "usr",
                    "lib",
                    "bin",
                    "x",
                    "1",
                    "data.mdb"
                )
            )
        }
        test("A path with only \"/\" should contain a list of only [\"/\"]") {
            assertSoftly(File("/").parts()) {
                size shouldBeExactly 1
                first() shouldBe "/"
            }
        }
    }
})
