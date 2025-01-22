package project;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;
import utils.Util;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Util.readJavaAstFromResource;
import static utils.Util.resourcePath;

class ProjectScannerTest {

    @Test
    public void shouldScanDirectoryProject() {
        ProjectScanner projectScanner = new ProjectScanner();

        Project project = projectScanner.scanProject(resourcePath("/"));

        assertThat(project)
                .usingRecursiveComparison()
                .ignoringFields("javaFiles.ast.storage")
                .isEqualTo(
                        new Project(
                                resourcePath("/"),
                                asList(
                                        new JavaFile(
                                                Paths.get("ShadowedVariableInNestedClass.java"),
                                                readJavaAstFromResource("/ShadowedVariableInNestedClass.java")
                                        ),
                                        new JavaFile(
                                                Paths.get("CorrectClass.java"),
                                                readJavaAstFromResource("/CorrectClass.java")
                                        ),
                                        new JavaFile(
                                                Paths.get("ShadowedVariableClass.java"),
                                                readJavaAstFromResource("/ShadowedVariableClass.java")
                                        )
                                )
                        )
                );
    }

    @Test
    public void shouldScanSingleFile() {
        ProjectScanner projectScanner = new ProjectScanner();

        Project project = projectScanner.scanProject(resourcePath("/CorrectClass.java"));

        assertThat(project)
                .usingRecursiveComparison()
                .ignoringFields("javaFiles.ast.storage")
                .isEqualTo(
                        new Project(
                                resourcePath("/CorrectClass.java"),
                                singletonList(
                                        new JavaFile(
                                                resourcePath("/CorrectClass.java"),
                                                readJavaAstFromResource("/CorrectClass.java")
                                        )
                                )
                        )
                );
    }
}