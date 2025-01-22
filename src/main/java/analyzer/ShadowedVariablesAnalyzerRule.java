package analyzer;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ShadowedVariablesAnalyzerRule implements AnalyzerRule {
    @Override
    public List<AnalyzerDefect> analyze(CompilationUnit ast) {
        return ast
                .findAll(ClassOrInterfaceDeclaration.class)
                .stream()
                .filter(classOrInterface -> !classOrInterface.isInterface())
                .flatMap(this::analyzeClass)
                .collect(toList());
    }

    private Stream<AnalyzerDefect> analyzeClass(ClassOrInterfaceDeclaration classDeclaration) {
        Map<String, VariableDeclarator> classVars = classDeclaration
                .getFields()
                .stream()
                .flatMap(field -> field.getVariables().stream())
                .collect(toMap(VariableDeclarator::getNameAsString, identity()));

        List<MethodDeclaration> methods = classDeclaration.getMethods();
        return methods
                .stream()
                .flatMap(method -> method.findAll(VariableDeclarator.class).stream())
                .filter(methodVar -> classVars.containsKey(methodVar.getNameAsString()))
                .map(methodVar -> {
                    VariableDeclarator classVar = classVars.get(methodVar.getNameAsString());
                    return classVar == null ? null : analyzerDefect(methodVar, classVar, classDeclaration);
                })
                .filter(Objects::nonNull);
    }

    private AnalyzerDefect analyzerDefect(
            VariableDeclarator shadowedVar,
            VariableDeclarator classVar,
            ClassOrInterfaceDeclaration classDeclaration
    ) {
        Position shadowedVarPosition = getPosition(shadowedVar);
        Position classVarPosition = getPosition(classVar);
        return new AnalyzerDefect(
                String.format(
                        "Variable '%s' at %d:%d shadows class variable '%s' at %d:%d in class %s",
                        shadowedVar.getNameAsString(),
                        shadowedVarPosition.line,
                        shadowedVarPosition.column,
                        classVar.getNameAsString(),
                        classVarPosition.line,
                        classVarPosition.column,
                        classDeclaration.getNameAsString()
                )
        );
    }

    private Position getPosition(VariableDeclarator varDeclarator) {
        Range range = varDeclarator.getRange().orElseThrow(() ->
                new IllegalStateException("Variable declarator should have range")
        );
        return range.begin;
    }
}
