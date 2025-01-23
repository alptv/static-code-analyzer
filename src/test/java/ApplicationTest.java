import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ApplicationTest {

    @Test
    public void shouldPrintHelpByOption() {
        String[] args = {"-h"};
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        System.setOut(new PrintStream(result));
        Application application = new Application();

        int exitCode = application.runWithExitCode(args);

        assertThat(exitCode).isEqualTo(0);
        String expectedOutput =
                "usage: analyzer [--help] [--src path]\n" +
                        " -h,--help        Getting help\n" +
                        " -s,--src <arg>   Source code file or directory. Default: current\n" +
                        "                  directory\n";
        assertThat(result.toString()).isEqualTo(expectedOutput);
    }

    @Test
    public void shouldPrintHelpOnParseException() {
        String[] args = {"-aaa"};
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        System.setOut(new PrintStream(result));
        Application application = new Application();

        int exitCode = application.runWithExitCode(args);

        assertThat(exitCode).isEqualTo(1);
        String expectedOutput =
                "usage: analyzer [--help] [--src path]\n" +
                        " -h,--help        Getting help\n" +
                        " -s,--src <arg>   Source code file or directory. Default: current\n" +
                        "                  directory\n";

        assertThat(result.toString()).isEqualTo(expectedOutput);
    }

    @Test
    public void shouldHandleFileProblems() {
        String[] args = {"-s './Notexisting.java'"};
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        System.setOut(new PrintStream(result));
        Application application = new Application();

        int exitCode = application.runWithExitCode(args);

        assertThat(exitCode).isEqualTo(1);
        String expectedOutput = "Failed to process input source file/directory.";
        assertThat(result.toString()).isEqualTo(expectedOutput);
    }

    @Test
    public void shouldReturnOneExitCodeInCaseOfDefects() {
        String[] args = {"-s", "src/test/resources/common"};
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        System.setOut(new PrintStream(result));
        Application application = new Application();

        int exitCode = application.runWithExitCode(args);

        assertThat(exitCode).isEqualTo(1);
        String expectedDefects =
                "Processing source code in: src/test/resources/common\n" +
                "Found following defects:\n" +
                "ShadowedVariableClass.java         : Variable 'a' at 7:13 shadows class variable 'a' at 3:9 in class ShadowedVariableClass\n" +
                "ShadowedVariableClass.java         : Variable 'b' at 11:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass\n" +
                "ShadowedVariableClass.java         : Variable 'b' at 8:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass\n" +
                "ShadowedVariableInNestedClass.java : Variable 'i' at 8:17 shadows class variable 'i' at 4:13 in class ShadowedVariableNestedClass\n";
        assertThat(result.toString()).isEqualTo(expectedDefects);
    }

    @Test
    public void shouldReturnZeroInCaseOfSuccess() {
        String[] args = {"-s", "src/test/resources/common/CorrectClass.java"};
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        System.setOut(new PrintStream(result));
        Application application = new Application();

        int exitCode = application.runWithExitCode(args);

        assertThat(exitCode).isEqualTo(0);
        String expectedReport =
                "Processing source code in: src/test/resources/common/CorrectClass.java\n" +
                "No defects found\n";
        assertThat(result.toString()).isEqualTo(expectedReport);
    }
}