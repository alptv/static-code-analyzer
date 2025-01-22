package project;

import java.nio.file.Path;
import java.util.List;

public class Project {
    private final Path projectPath;
    private final List<JavaFile> javaFiles;

    public Project(Path projectPath, List<JavaFile> javaFiles) {
        this.projectPath = projectPath;
        this.javaFiles = javaFiles;
    }

    public List<JavaFile> getJavaFiles() {
        return javaFiles;
    }

    public Path getProjectPath() {
        return projectPath;
    }
}

