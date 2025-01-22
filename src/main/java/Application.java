import analyzer.AnalyzerDefect;
import analyzer.AnalyzerRule;
import analyzer.ShadowedVariablesAnalyzerRule;
import project.Project;
import project.ProjectScanner;
import projectanalyzer.ProjectAnalyzer;
import projectanalyzer.ProjectDefectsReporter;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

import static java.util.Arrays.asList;

public class Application {
    public boolean detectDefects(Path projectPath) {
        Project project = scanProject(projectPath);
        ProjectAnalyzer projectAnalyzer = createProjectAnalyzer(project);
        List<AnalyzerDefect> projectDefects = projectAnalyzer.analyze();
        try (Writer output = output()) {
            ProjectDefectsReporter projectDefectsReporter = new ProjectDefectsReporter(project, output);
            projectDefectsReporter.report(projectDefects);
            return !projectDefects.isEmpty();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Project scanProject(Path projectPath) {
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
