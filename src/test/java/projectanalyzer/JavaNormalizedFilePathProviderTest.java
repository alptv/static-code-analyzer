package projectanalyzer;

import org.junit.jupiter.api.Test;
import project.JavaFile;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JavaNormalizedFilePathProviderTest {

    @Test
    public void shouldTrimLongDirectories() {
        JavaFile longDirJavaFile = new JavaFile(Paths.get("looooooooooooooooooooooooooooooooongdir/file"));
        List<JavaFile> javaFiles = singletonList(longDirJavaFile);
        JavaNormalizedFilePathProvider filePathProvider = new JavaNormalizedFilePathProvider(javaFiles);

        String filePath = filePathProvider.getNormalizedPath(longDirJavaFile);

        assertThat(filePath).isEqualTo("...oooooooooongdir/file");
    }

    @Test
    public void shouldNotTrimShortDirectories() {
        JavaFile shortDirJavaFile = new JavaFile(Paths.get("dir/file"));
        List<JavaFile> javaFiles = singletonList(shortDirJavaFile);
        JavaNormalizedFilePathProvider filePathProvider = new JavaNormalizedFilePathProvider(javaFiles);

        String filePath = filePathProvider.getNormalizedPath(shortDirJavaFile);

        assertThat(filePath).isEqualTo("dir/file");
    }

    @Test
    public void shouldIndentFileNamesToSameLength() {
        JavaFile shortNameJavaFile = new JavaFile(Paths.get("dirrr/file"));
        JavaFile longNameJavaFile = new JavaFile(Paths.get("dir/loooooooooooooooooooooooooongfile"));
        List<JavaFile> javaFiles = asList(shortNameJavaFile, longNameJavaFile);
        JavaNormalizedFilePathProvider filePathProvider = new JavaNormalizedFilePathProvider(javaFiles);

        String shortNameFilePath = filePathProvider.getNormalizedPath(shortNameJavaFile);
        String longNameFilePath = filePathProvider.getNormalizedPath(longNameJavaFile);

        assertThat(shortNameFilePath).isEqualTo("dirrr/file                           ");
        assertThat(longNameFilePath).isEqualTo("dir/loooooooooooooooooooooooooongfile");
    }

}