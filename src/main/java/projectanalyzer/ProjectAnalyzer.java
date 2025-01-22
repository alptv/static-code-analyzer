package projectanalyzer;

import analyzer.AnalyzerDefect;
import analyzer.AnalyzerRule;
import project.JavaFile;
import project.Project;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ProjectAnalyzer {
    public static final String CLASS_LOCATION_SEP = " : ";
    public static final String SYNTAX_ERROR = "Syntax error";
    private final Project project;
    private final List<AnalyzerRule> analyzerRules;

    public ProjectAnalyzer(
            Project project,
            List<AnalyzerRule> analyzerRules
    ) {
        this.project = project;
        this.analyzerRules = analyzerRules;
    }

    public List<AnalyzerDefect> analyze() {
        Map<JavaFile, List<AnalyzerDefect>> defectsByJavaFile = project.getJavaFiles()
                .stream()
                .map(javaFile -> new SimpleEntry<>(javaFile, analyzeJavaFile(javaFile)))
                .filter(javaFileWithDefects -> !javaFileWithDefects.getValue().isEmpty())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        JavaNormalizedFilePathProvider filePathProvider = new JavaNormalizedFilePathProvider(defectsByJavaFile.keySet());

        return defectsByJavaFile
                .entrySet()
                .stream()
                .flatMap(javaFileWithDefects ->
                        javaFileWithDefects
                                .getValue()
                                .stream()
                                .map(defect ->
                                        extendAnalyzerDefectMessage(defect, filePathProvider.getNormalizedPath(javaFileWithDefects.getKey()))
                                )
                )
                .collect(toList());
    }

    private List<AnalyzerDefect> analyzeJavaFile(JavaFile javaFile) {
        if (javaFile.isAstCorrectlyParsed()) {
            return analyzeAst(javaFile);
        } else {
            return singletonList(new AnalyzerDefect(SYNTAX_ERROR));
        }
    }

    private List<AnalyzerDefect> analyzeAst(JavaFile javaFile) {
        return analyzerRules
                .stream()
                .flatMap(rule -> rule.analyze(javaFile.getAst()).stream())
                .collect(toList());
    }

    private AnalyzerDefect extendAnalyzerDefectMessage(AnalyzerDefect analyzerDefectMessage, String normalizedPath) {
        return new AnalyzerDefect(normalizedPath + CLASS_LOCATION_SEP + analyzerDefectMessage.getMessage());
    }
}
