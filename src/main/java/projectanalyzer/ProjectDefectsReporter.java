package projectanalyzer;

import analyzer.AnalyzerDefect;
import project.Project;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

public class ProjectDefectsReporter {
    private final Project project;
    private final Writer output;

    public ProjectDefectsReporter(Project project, Writer output) {
        this.project = project;
        this.output = output;
    }

    public void report(List<AnalyzerDefect> projectDefects) throws IOException {
        writeln("Processing source code in: " + project.getProjectPath());
        if (projectDefects.isEmpty()) {
            writeln("No defects found");
            return;
        }
        writeln("Found following defects:");
        for (AnalyzerDefect defect : projectDefects) {
            writeln(defect.getMessage());
        }
    }

    private void writeln(String string) throws IOException {
        output.write(string);
        output.write(System.lineSeparator());
    }
}
