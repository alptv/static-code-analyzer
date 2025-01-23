public class Main {

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        int exitCode = new Application().runWithExitCode(args);
        System.exit(exitCode);
    }
}