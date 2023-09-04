import andriesfc.mpt.foundation.FractionSeekingStrategy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static andriesfc.mpt.foundation.Foundation.fraction;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFoundationFromJava {

    @Nested
    class FindingFractions {
        private final double pi;

        FindingFractions() {
            pi = Math.PI;
        }

        @Test
        void usingSimple() {
            var fraction = assertDoesNotThrow(() -> fraction(pi, FractionSeekingStrategy.Simple.INSTANCE));
            var piAgain = 3 + fraction;
            assertEquals(pi, piAgain);
        }

        @Test
        void usingHorners() {
            var fraction = assertDoesNotThrow(() -> fraction(pi, FractionSeekingStrategy.Horners.INSTANCE));
            var piAgain = 3 + fraction;
            assertEquals(pi, piAgain);
        }
    }

}
