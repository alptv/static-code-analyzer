package project;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.github.javaparser.utils.SourceRoot.Callback.Result.DONT_SAVE;

public class ProjectScanner {

    public Project scanProject(Path projectPath) throws IOException {
        if (!Files.exists(projectPath)) throw new FileNotFoundException(projectPath.toString());

        List<JavaFile> javaFiles = new ArrayList<>();
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
    }

    private JavaFile javaFile(ParseResult<CompilationUnit> javaFileParseResult, Path path) {
        if (!javaFileParseResult.isSuccessful()) return new JavaFile(path);
        CompilationUnit ast = javaFileParseResult.getResult().get();
        return new JavaFile(path, ast);
    }
}
