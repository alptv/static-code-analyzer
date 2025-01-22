package analyzer;

import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

public interface AnalyzerRule {
    List<AnalyzerDefect> analyze(CompilationUnit ast);
}
