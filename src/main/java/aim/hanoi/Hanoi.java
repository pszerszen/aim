package aim.hanoi;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

@Log4j2
public class Hanoi {

    private static final String BASE_FILE_PATH = String.join(File.separator,
            System.getProperty("user.home"),
            "workspace", "aim", "src", "main", "resources", "aim", "hanoi")
            .concat(File.separator);

    private static final String DISKS = "d";

    private static final String HELP = "h";

    private static final Options OPTIONS = new Options()
            .addOption(new Option(DISKS, "disks", true, "Number of disks."))
            .addOption(new Option(HELP, "help", false, "Print this message."));

    @SuppressWarnings("SimplifyStreamApiCallChains")
    public static void main(String... args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(OPTIONS, args);

        if (line.hasOption(HELP)) {
            printHelpAndExit(OPTIONS);
        }

        String disksValue = line.getOptionValue(DISKS, "3");
        int numberOfDisks = Integer.parseInt(disksValue);

        log.info("Number of disks: {}", numberOfDisks);
        List<Towers> bfsPath = Algorithms.iterativeBfs(numberOfDisks);
        List<Towers> dfsPath = Algorithms.iterativeDfs(numberOfDisks);
        List<Towers> aStarPath = Algorithms.aStar(numberOfDisks);

        log.info("BFS solution ({} steps):", bfsPath.size());
        log.info("DFS solution ({} steps):", dfsPath.size());
        log.info("A* solution ({} steps):", aStarPath.size());

        saveResultsToFile(bfsPath, getFile("bfs"), numberOfDisks);
        saveResultsToFile(dfsPath, getFile("dfs"), numberOfDisks);
        saveResultsToFile(aStarPath, getFile("aStar"), numberOfDisks);
    }

    @SneakyThrows
    private static void saveResultsToFile(List<Towers> path, File file, int numberOfDisks) {
        Writer writer = new FileWriter(file, false)
                .append(String.format("Number of disks: %s", numberOfDisks))
                .append(System.lineSeparator())
                .append(String.format("Solution takes %s steps:", path.size()))
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        for (Towers step : path) {
            writer.append(step.toString());
            writer.append(System.lineSeparator());
        }

        writer.close();
    }

    private static File getFile(String name) {
        new File(BASE_FILE_PATH).mkdirs();
        return new File(BASE_FILE_PATH + name + ".txt");
    }

    private static void printHelpAndExit(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(Hanoi.class.getSimpleName(), options);
        System.exit(0);
    }
}
