package project;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.javaparser.utils.SourceRoot.Callback.Result.DONT_SAVE;
import static java.util.stream.Collectors.toList;

public class ProjectScanner {

    public Project scanProject(Path projectPath) {
        List<JavaFile> javaFiles = new ArrayList<>();
        try {
            if (Files.isDirectory(projectPath)) {
                SourceRoot projectSourceRoot = new SourceRoot(projectPath);
                projectSourceRoot.parse("", (localPath, absolutePath, parseResult) -> {
                    javaFiles.add(javaFile(parseResult, localPath));
                    return DONT_SAVE;
                });
            } else {
                SourceRoot projectSourceRoot = new SourceRoot(projectPath.getParent());
                String fileName = projectPath.getFileName().toString();
                ParseResult<CompilationUnit> parseResult = projectSourceRoot.tryToParse("", fileName);
                javaFiles.add(javaFile(parseResult, projectPath));
            }
            return new Project(projectPath, javaFiles);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private JavaFile javaFile(ParseResult<CompilationUnit> javaFileParseResult, Path path) {
        if (!javaFileParseResult.isSuccessful()) return new JavaFile(path);
        Optional<CompilationUnit> ast = javaFileParseResult.getResult();
        return ast
                .map(compilationUnit -> new JavaFile(path, compilationUnit))
                .orElseGet(() -> new JavaFile(path));
    }
}
