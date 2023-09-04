package andriesfc.mtp.file;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;

import static andriesfc.mtp.file.FileObjects.file;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaAccessingFileObjectsTest {

    @Nested
    class BuildFileFromPathParts {

        @Test
        void testBuildPath() {
            var file = file("~", ".local", "data", "1");
            var expected = new File(System.getProperty("user.home") + "/.local/data/1");
            System.out.printf("Actual: %s%n", file);
            assertEquals(expected, file);
        }
    }

}
