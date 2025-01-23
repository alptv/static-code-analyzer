import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class Application {
    private final PrintWriter output = new PrintWriter(System.out);
    private final CommandLineParser parser = new DefaultParser();
    private final HelpFormatter formatter = new HelpFormatter();
    private final Options options = new Options()
            .addOption("h", "help", false, "Getting help")
            .addOption("s", "src", true, "Source code file or directory. Default: current directory");

    public int runWithExitCode(String[] args) {
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                printHelp();
            } else {
                String path = commandLine.getOptionValue('s', "./");
                boolean hasDefects = new DefectsDetector().detectDefects(Paths.get(path));
                if (hasDefects) {
                    return 1;
                }
            }
        } catch (ParseException e) {
            printHelp();
            return 1;
        } catch (IOException e) {
            output.print("Failed to process input source file/directory.");
            output.flush();
            return 1;
        }
        return 0;
    }
    private void printHelp() {
        formatter.printHelp("analyzer [--help] [--src path]", options);
    }
}
