package projectanalyzer;

import project.JavaFile;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.toMap;

public class JavaNormalizedFilePathProvider {
    private static final int MAX_FILE_PATH_SIZE = 15;
    private static final String SPACE = " ";
    private static final String DOTS = "...";
    private static final String PATH_SEP = "/";
    private final Map<JavaFile, String> javaFileNormalizedPaths;

    public JavaNormalizedFilePathProvider(Collection<JavaFile> javaFiles) {
        Map<JavaFile, String> trimmedJavaFilePaths = javaFiles
                .stream()
                .collect(toMap(Function.identity(), this::trimmedJavaFilePath));
        int maxJavaFilePathLength = trimmedJavaFilePaths.values().stream().mapToInt(String::length).max().orElse(0);
        this.javaFileNormalizedPaths = trimmedJavaFilePaths.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> normalizedJavaFilePath(e.getValue(), maxJavaFilePathLength)));

    }
    public String getNormalizedPath(JavaFile javaFile) {
        return javaFileNormalizedPaths.get(javaFile);
    }
    private String normalizedJavaFilePath(String javaFilePath, int maxJavaFilePathLength) {
        return javaFilePath + String.join("", nCopies(maxJavaFilePathLength - javaFilePath.length(), SPACE));
    }

    private String trimmedJavaFilePath(JavaFile javaFile) {
        String javaFileDir = javaFileDir(javaFile);
        int javaFileDirSize = javaFileDir.length();
        if (javaFileDirSize > MAX_FILE_PATH_SIZE) {
            javaFileDir = DOTS + javaFileDir.substring(javaFileDirSize - MAX_FILE_PATH_SIZE);
        }
        if (!javaFileDir.isEmpty()) {
            javaFileDir += PATH_SEP;
        }
        return javaFileDir + javaFile.getFileName();
    }

    private String javaFileDir(JavaFile javaFile) {
        Path javaFileDirPath = javaFile.getFilePath().getParent();
        return javaFileDirPath == null ? "" : javaFileDirPath.toString();
    }
}
