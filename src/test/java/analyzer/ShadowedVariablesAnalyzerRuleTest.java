package analyzer;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.Util.readJavaAstFromResource;

class ShadowedVariablesAnalyzerRuleTest {
    private final ShadowedVariablesAnalyzerRule analyzerRule = new ShadowedVariablesAnalyzerRule();
    @Test
    public void shouldReturnNoDefectsOnCorrectClass() {
        CompilationUnit correctAst = readJavaAstFromResource("/CorrectClass.java");

        List<AnalyzerDefect> defects = analyzerRule.analyze(correctAst);

        assertThat(defects).isEmpty();
    }


    @Test
    public void shouldReturnDefectsOnIncorrect() {
        CompilationUnit correctAst = readJavaAstFromResource("/ShadowedVariableClass.java");

        List<AnalyzerDefect> defects = analyzerRule.analyze(correctAst);

        assertThat(defects)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new AnalyzerDefect("Variable 'a' at 7:13 shadows class variable 'a' at 3:9 in class ShadowedVariableClass"),
                        new AnalyzerDefect("Variable 'b' at 8:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass"),
                        new AnalyzerDefect("Variable 'b' at 11:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass")
                );
    }

    @Test
    public void shouldReturnDefectsOnIncorrectNestedClass() {
        CompilationUnit correctAst = readJavaAstFromResource("/ShadowedVariableInNestedClass.java");

        List<AnalyzerDefect> defects = analyzerRule.analyze(correctAst);

        assertThat(defects)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new AnalyzerDefect("Variable 'i' at 17:17 shadows class variable 'i' at 13:13 in class ShadowedVariableNestedClass")
                );
    }
}