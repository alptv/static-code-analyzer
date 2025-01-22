import org.apache.commons.cli.*;

import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    private final CommandLineParser parser = new DefaultParser();
    private final HelpFormatter formatter = new HelpFormatter();
    private final Options options = new Options()
            .addOption("h", "help", false, "Getting help")
            .addOption("s", "src", true, "Source code file or directory. Default: current directory");


    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                printHelp();
            } else {
                String path = commandLine.getOptionValue('s', "./");
                boolean hasDefects = new Application().detectDefects(Paths.get(path));
                if (hasDefects) {
                    System.exit(1);
                }
            }
        } catch (ParseException e) {
            printHelp();
            System.exit(1);
        }
    }
    private void printHelp() {
        formatter.printHelp("analyzer [--help] [--src path]", options);
    }
}