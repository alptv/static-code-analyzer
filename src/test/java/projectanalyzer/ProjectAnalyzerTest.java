package projectanalyzer;

import analyzer.AnalyzerDefect;
import analyzer.AnalyzerRule;
import org.junit.jupiter.api.Test;
import project.JavaFile;
import project.Project;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.Util.readJavaAstFromResource;
import static utils.Util.resourcePath;

class ProjectAnalyzerTest {
    private final AnalyzerRule defectAnalyzerRule = ast -> singletonList(new AnalyzerDefect("defect"));
    private final AnalyzerRule errorAnalyzerRule = ast -> singletonList(new AnalyzerDefect("error"));

    @Test
    public void shouldRunAllRulesAndExtendThemWithFileInfoAndSyntaxError() {
        Project project = new Project(
                resourcePath("/"),
                asList(
                        new JavaFile(
                                Paths.get("looooooooooooooooongdir/File.java")
                        ),
                        new JavaFile(
                                Paths.get("dir/CorrectClass.java"),
                                readJavaAstFromResource("/common/CorrectClass.java")
                        )
                )
        );
        ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(
                project,
                asList(defectAnalyzerRule, errorAnalyzerRule)
        );

        List<AnalyzerDefect> defects = projectAnalyzer.analyze();

        assertThat(defects).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
                new AnalyzerDefect("dir/CorrectClass.java        : defect"),
                new AnalyzerDefect("dir/CorrectClass.java        : error"),
                new AnalyzerDefect("...oooooooooongdir/File.java : Syntax error")
        );
    }
}