package projectanalyzer;

import analyzer.AnalyzerDefect;
import project.Project;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDefectsReporter {
    private final Project project;
    private final Writer output;

    public ProjectDefectsReporter(Project project, Writer output) {
        this.project = project;
        this.output = output;
    }

    public void report(List<AnalyzerDefect> projectDefects) throws IOException {
        List<String> sortedDefectMessages = projectDefects
                .stream()
                .map(AnalyzerDefect::getMessage)
                .sorted()
                .collect(Collectors.toList());
        writeln("Processing source code in: " + project.getProjectPath());
        if (projectDefects.isEmpty()) {
            writeln("No defects found");
            return;
        }
        writeln("Found following defects:");
        for (String defect : sortedDefectMessages) {
            writeln(defect);
        }
    }

    private void writeln(String string) throws IOException {
        output.write(string);
        output.write(System.lineSeparator());
    }
}
