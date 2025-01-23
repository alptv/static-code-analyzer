package analyzer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.Util.readJavaAstFromResource;

class ShadowedVariablesAnalyzerRuleTest {
    private final ShadowedVariablesAnalyzerRule analyzerRule = new ShadowedVariablesAnalyzerRule();
    @Test
    public void shouldReturnNoDefectsOnCorrectClass() {
        CompilationUnit correctAst = readJavaAstFromResource("/common/CorrectClass.java");

        List<AnalyzerDefect> defects = analyzerRule.analyze(correctAst);

        assertThat(defects).isEmpty();
    }


    @Test
    public void shouldReturnDefectsOnIncorrect() {
        CompilationUnit shadowedVariableAst = readJavaAstFromResource("/common/ShadowedVariableClass.java");

        List<AnalyzerDefect> defects = analyzerRule.analyze(shadowedVariableAst);

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
        CompilationUnit nestedClassAst = readJavaAstFromResource("/common/ShadowedVariableInNestedClass.java");

        List<AnalyzerDefect> defects = analyzerRule.analyze(nestedClassAst);

        assertThat(defects)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new AnalyzerDefect("Variable 'i' at 8:17 shadows class variable 'i' at 4:13 in class ShadowedVariableNestedClass")
                );
    }

    @Test
    public void invalidUsageCaseWhenVariableHasNoPosition() {
        CompilationUnit astWithOutsideAddedVariable = readJavaAstFromResource("/common/ShadowedVariableInNestedClass.java");
        ClassOrInterfaceDeclaration classToAddVariable = astWithOutsideAddedVariable
                .findAll(ClassOrInterfaceDeclaration.class)
                .stream()
                .filter(clazz -> clazz.getNameAsString().equals("ShadowedVariableNestedClass"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Illegal state of test: ShadowedVariableNestedClass not found"));
        classToAddVariable.addField(int.class, "outsideAddedVariable");
        BlockStmt methodBody = classToAddVariable
                .getMethodsByName("z")
                .get(0)
                .getBody()
                .orElseThrow(() -> new IllegalStateException("Illegal state of test: z method in ShadowedVariableNestedClass not found"));
        methodBody.addStatement("int outsideAddedVariable = 1;");

        assertThatThrownBy(() -> analyzerRule.analyze(astWithOutsideAddedVariable))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Variable declarator should have range");
    }
}