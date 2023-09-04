package andriesfc.mpt.foundation.functional

import io.kotest.assertions.asClue
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month

class ProviderKtTest : FunSpec({

    context("Constructing provider from a single value") {
        val expected = 10
        val provider = providerOf(expected)
        test("Should return the same value") {
            provider.get() shouldBe expected
        }
        test("Mapping values from a single value") {
            val p = provider.map { "$it" }
            p.get().shouldBe(10.toString())
        }
        context("Correctly handles equality") {
            val copy = providerOf(expected)
            val different = providerOf(6)
            assertSoftly {
                provider shouldBeEqual copy
                provider shouldNotBeEqual different
            }
        }
    }

    context("Constructing a provider from supplier") {

        val supplier: () -> LocalDateTime = mockk("mockedSupplier")
        val provider = providerOf(supplier)
        lateinit var expected: LocalDateTime

        beforeTest {
            clearMocks(supplier)
            every { supplier.invoke() } answers { expected }
            expected = LocalDateTime.of(LocalDate.of(2022, Month.JULY, 16), LocalTime.now())
        }

        test("Returning the value from a supplier") {
            val actual = provider.get()
            actual.shouldBeEqual(expected)
            verify(exactly = 1) { supplier.invoke() }
            confirmVerified(supplier)
        }

        test("Mapping using providers") {
            val p = provider.map { it.minusMinutes(10) }
            verify { supplier.invoke() wasNot Called }
            val actual = p.get()
            verify(exactly = 1) { supplier.invoke() }
            confirmVerified(supplier)
            actual shouldBeEqual expected.minusMinutes(10)
        }

        context("Handles equality correctly") {
            val copy = providerOf(supplier)
            val anotherToFunctionNow = providerOf(LocalDateTime::now)
            test("Copy should be treated as equal") {
                assertSoftly {
                    copy.asClue { it shouldBeEqual  provider  }
                    anotherToFunctionNow.asClue { it shouldNotBeEqual   provider }
                }
            }

        }
    }

})
