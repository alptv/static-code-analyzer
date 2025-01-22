import org.junit.jupiter.api.Test;
import utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    public void shouldRunAnalyzerAtSpecificDirAndReportAllReports() {
        Path path = Util.resourcePath("/");
        ByteArrayOutputStream reportedDefects = new ByteArrayOutputStream();
        System.setOut(new PrintStream(reportedDefects));
        Application application = new Application();

        application.detectDefects(path);

        String expectedDefects =
                "Processing source code in: src/test/resources\n" +
                "Found following defects:\n" +
                "ShadowedVariableClass.java         : Variable 'a' at 7:13 shadows class variable 'a' at 3:9 in class ShadowedVariableClass\n" +
                "ShadowedVariableClass.java         : Variable 'b' at 8:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass\n" +
                "ShadowedVariableClass.java         : Variable 'b' at 11:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass\n" +
                "ShadowedVariableInNestedClass.java : Variable 'i' at 17:17 shadows class variable 'i' at 13:13 in class ShadowedVariableNestedClass\n";

        assertThat(reportedDefects.toString()).isEqualTo(expectedDefects);
    }

}