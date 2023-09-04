package andriesfc.mpt.foundation.functional

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldNotBeInstanceOf
import java.math.BigDecimal

class EitherTest : FunSpec({

    context("Creating results") {
        test("success") {
            val r: Either<Int, String> = "1".success()
            r.getOrNull().shouldBe("1")
        }
        test("failure") {
            val r: Either<Int, String> = 10.failure()
            r.shouldNotBeInstanceOf<Either.Success<String>>()
            r.shouldBeInstanceOf<Either.Failure<Int>>()
        }
        context("Using responseOf { ...} ") {


        }
    }

})

interface RentalCalculator {

    sealed class Error() {
        abstract val code: Int
        abstract val message: String
    }

    fun calcRentalPayment(

    ): Either<Error, BigDecimal>
}
