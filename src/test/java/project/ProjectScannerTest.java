package project;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.Util.readJavaAstFromResource;
import static utils.Util.resourcePath;

class ProjectScannerTest {

    @Test
    public void shouldScanDirectoryProject() throws IOException {
        ProjectScanner projectScanner = new ProjectScanner();

        Project project = projectScanner.scanProject(resourcePath("/project"));

        assertThat(project)
                .usingRecursiveComparison()
                .ignoringFields("javaFiles.ast.storage")
                .isEqualTo(
                        new Project(
                                resourcePath("/project"),
                                asList(
                                        new JavaFile(
                                                Paths.get("CorrectClass.java"),
                                                readJavaAstFromResource("/project/CorrectClass.java")
                                        ),
                                        new JavaFile(Paths.get("SyntaxErrorClass.java"))
                                )
                        )
                );
    }

    @Test
    public void shouldScanSingleFile() throws IOException {
        ProjectScanner projectScanner = new ProjectScanner();

        Project project = projectScanner.scanProject(resourcePath("/common/CorrectClass.java"));

        assertThat(project)
                .usingRecursiveComparison()
                .ignoringFields("javaFiles.ast.storage")
                .isEqualTo(
                        new Project(
                                resourcePath("/common/CorrectClass.java"),
                                singletonList(
                                        new JavaFile(
                                                resourcePath("/common/CorrectClass.java"),
                                                readJavaAstFromResource("/common/CorrectClass.java")
                                        )
                                )
                        )
                );
    }
}