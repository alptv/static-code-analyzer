package projectanalyzer;

import analyzer.AnalyzerDefect;
import org.junit.jupiter.api.Test;
import project.Project;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ProjectDefectsReporterTest {

    @Test
    public void shouldReportDefectsIfExists() throws IOException {
        Project project = new Project(Paths.get("project/path"), emptyList());
        StringWriter stringWriter = new StringWriter();
        ProjectDefectsReporter reporter = new ProjectDefectsReporter(project, stringWriter);

        List<AnalyzerDefect> defects = Arrays.asList(
                new AnalyzerDefect("analyzer defect 1"),
                new AnalyzerDefect("analyzer defect 2")
        );

        reporter.report(defects);

        String expectedReport =
                "Processing source code in: project/path\n" +
                "Found following defects:\n" +
                "analyzer defect 1\n" +
                "analyzer defect 2\n";
        assertThat(stringWriter.toString()).isEqualTo(expectedReport);
    }

    @Test
    public void shouldNotReportDefectsIfNotExists() throws IOException {
        Project project = new Project(Paths.get("project/path"), emptyList());
        StringWriter stringWriter = new StringWriter();
        ProjectDefectsReporter reporter = new ProjectDefectsReporter(project, stringWriter);

        reporter.report(emptyList());

        String expectedReport =
                "Processing source code in: project/path\n" +
                        "No defects found\n";
        assertThat(stringWriter.toString()).isEqualTo(expectedReport);
    }

}