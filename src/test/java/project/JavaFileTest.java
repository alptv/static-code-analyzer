package project;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;
import utils.Util;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JavaFileTest {

    @Test
    public void shouldReturnFilePath() {
        Path path = Paths.get("some/path");

        JavaFile javaFile = new JavaFile(path);

        assertThat(javaFile.getFilePath()).isEqualTo(path);
    }

    @Test
    public void shouldReturnFileName() {
        Path path = Paths.get("some/path");

        JavaFile javaFile = new JavaFile(path);

        assertThat(javaFile.getFileName()).isEqualTo("path");
    }

    @Test
    public void shouldReturnAst() {
        Path path = Paths.get("some/path");
        CompilationUnit ast = Util.readJavaAstFromResource("/CorrectClass.java");

        JavaFile javaFile = new JavaFile(path, ast);

        assertThat(javaFile.getAst()).isEqualTo(ast);
    }

    @Test
    public void shouldBeCorrectlyParsed() {
        Path path = Paths.get("some/path");
        CompilationUnit ast = Util.readJavaAstFromResource("/CorrectClass.java");

        JavaFile javaFile = new JavaFile(path, ast);

        assertThat(javaFile.isAstCorrectlyParsed()).isTrue();
    }

    @Test
    public void shouldNotBeCorrectlyParsed() {
        Path path = Paths.get("some/path");

        JavaFile javaFile = new JavaFile(path);

        assertThat(javaFile.isAstCorrectlyParsed()).isFalse();
    }


    @Test
    public void shouldThrowExceptionIfNoAst() {
        Path path = Paths.get("some/path");

        JavaFile javaFile = new JavaFile(path);

        assertThatThrownBy(javaFile::getAst)
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Ast can not be extracted from incorrect java file");
    }

}