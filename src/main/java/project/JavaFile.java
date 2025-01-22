package project;

import com.github.javaparser.ast.CompilationUnit;

import java.nio.file.Path;

public class JavaFile {
    private final Path filePath;
    private final CompilationUnit ast;

    public JavaFile(Path filePath) {
        this(filePath, null);
    }
    public JavaFile(Path filePath, CompilationUnit ast) {
        this.filePath = filePath;
        this.ast = ast;
    }
    public Path getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return filePath.getFileName().toString();
    }

    public CompilationUnit getAst() {
        if (ast == null) {
            throw new IllegalStateException("Ast can not be extracted from incorrect java file");
        }
        return ast;
    }

    public boolean isAstCorrectlyParsed() {
        return ast != null;
    }
}
