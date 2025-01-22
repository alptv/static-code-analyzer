package utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

public class Util {

    public static CompilationUnit readJavaAstFromResource(String path) {
        try (InputStream resource = Util.class.getResourceAsStream(path)) {
            if (resource == null) {
                throw new IllegalStateException(format("Resource with path=%s not found", path));
            }

            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(resource);
            return parseResult.getResult().orElseThrow(
                    () -> new IllegalStateException(format("Invalid syntax in resource with path=%s", path))
            );

        } catch (IOException e) {
            throw new IllegalStateException(format("Failed to read resource with path=%s", path));
        }
    }

    public static Path resourcePath(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return Paths.get("src/test/resources/" + path);
    }
}
