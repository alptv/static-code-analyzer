package analyzer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

class AnalyzerDefectTest {

    @Test
    public void shouldReturnMessage() {
        AnalyzerDefect analyzerDefect = new AnalyzerDefect("message");

        assertThat(analyzerDefect.getMessage()).isEqualTo("message");
    }

}