package project;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProjectTest {

    @Test
    public void shouldReturnJavaFiles() {
        Path path = Paths.get("some");
        List<JavaFile> javaFiles = Arrays.asList(
          new JavaFile(Paths.get("some/path1")),
          new JavaFile(Paths.get("some/path2"))
        );
        Project project = new Project(path, javaFiles);

        assertThat(project.getJavaFiles()).isEqualTo(javaFiles);
    }

    @Test
    public void shouldReturnProjectPath() {
        List<JavaFile> javaFiles = Arrays.asList(
                new JavaFile(Paths.get("some/path1")),
                new JavaFile(Paths.get("some/path2"))
        );
        Path path = Paths.get("some");

        Project project = new Project(path, javaFiles);

        assertThat(project.getProjectPath()).isEqualTo(path);
    }
}