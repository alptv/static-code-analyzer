import org.junit.jupiter.api.Test;
import utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DefectsDetectorTest {

    @Test
    public void shouldRunAnalyzerAtSpecificDirAndReportErrors() throws IOException {
        Path path = Util.resourcePath("/common");
        ByteArrayOutputStream reportedDefects = new ByteArrayOutputStream();
        System.setOut(new PrintStream(reportedDefects));
        DefectsDetector application = new DefectsDetector();

        assertThat(application.detectDefects(path)).isTrue();

        String expectedDefects =
                "Processing source code in: src/test/resources/common\n" +
                "Found following defects:\n" +
                "ShadowedVariableClass.java         : Variable 'a' at 7:13 shadows class variable 'a' at 3:9 in class ShadowedVariableClass\n" + "ShadowedVariableClass.java         : Variable 'b' at 11:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass\n" + "ShadowedVariableClass.java         : Variable 'b' at 8:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass\n" +
                "ShadowedVariableInNestedClass.java : Variable 'i' at 8:17 shadows class variable 'i' at 4:13 in class ShadowedVariableNestedClass\n";

        assertThat(reportedDefects.toString()).isEqualTo(expectedDefects);
    }

    @Test
    public void shouldRunAnalyzerAtSpecificDirAndReportSuccess() throws IOException {
        Path path = Util.resourcePath("/common/CorrectClass.java");
        ByteArrayOutputStream reportedDefects = new ByteArrayOutputStream();
        System.setOut(new PrintStream(reportedDefects));
        DefectsDetector defectsDetector = new DefectsDetector();

        assertThat(defectsDetector.detectDefects(path)).isFalse();

        String expectedDefects =
                "Processing source code in: src/test/resources/common/CorrectClass.java\n" +
                "No defects found\n";

        assertThat(reportedDefects.toString()).isEqualTo(expectedDefects);
    }
}