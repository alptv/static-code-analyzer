import analyzer.AnalyzerDefect;
import analyzer.AnalyzerRule;
import analyzer.ShadowedVariablesAnalyzerRule;
import project.Project;
import project.ProjectScanner;
import projectanalyzer.ProjectAnalyzer;
import projectanalyzer.ProjectDefectsReporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

import static java.util.Arrays.asList;

public class DefectsDetector {
    public boolean detectDefects(Path projectPath) throws IOException {
        Project project = scanProject(projectPath);
        ProjectAnalyzer projectAnalyzer = createProjectAnalyzer(project);
        List<AnalyzerDefect> projectDefects = projectAnalyzer.analyze();
        try (Writer output = output()) {
            ProjectDefectsReporter projectDefectsReporter = new ProjectDefectsReporter(project, output);
            projectDefectsReporter.report(projectDefects);
            return !projectDefects.isEmpty();
        }
    }

    private Project scanProject(Path projectPath) throws IOException {
        return new ProjectScanner().scanProject(projectPath);
    }

    private ProjectAnalyzer createProjectAnalyzer(Project project) {
        List<AnalyzerRule> analyzerRules = asList(
                new ShadowedVariablesAnalyzerRule()
        );
        return new ProjectAnalyzer(project, analyzerRules);
    }

    private Writer output() {
        return new BufferedWriter(new OutputStreamWriter(System.out)) {
            @Override
            public void close() throws IOException {
                flush();
            }
        };
    }
}
