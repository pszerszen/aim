package aim.hanoi;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Log4j2
public class Hanoi {

    @SuppressWarnings("SimplifyStreamApiCallChains")
    public static void main(String... args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(TowersUtils.OPTIONS, args);

        if (line.hasOption(TowersUtils.HELP)) {
            TowersUtils.printHelpAndExit(TowersUtils.OPTIONS);
        }

        String disksValue = line.getOptionValue(TowersUtils.DISKS, "3");
        int numberOfDisks = Integer.parseInt(disksValue);
        log.info("Number of disks: {}", numberOfDisks);

        List<Towers> bfsPath = runAlgorithm(Algorithm.BFS, numberOfDisks);
        List<Towers> dfsPath = runAlgorithm(Algorithm.DFS, numberOfDisks);
        List<Towers> aStarPath = runAlgorithm(Algorithm.A_STAR, numberOfDisks);

        TowersUtils.saveResultsToFile(bfsPath, TowersUtils.getFile("bfs"), numberOfDisks);
        TowersUtils.saveResultsToFile(dfsPath, TowersUtils.getFile("dfs"), numberOfDisks);
        TowersUtils.saveResultsToFile(aStarPath, TowersUtils.getFile("aStar"), numberOfDisks);
    }

    private static List<Towers> runAlgorithm(Algorithm algorithm, int numberOfDisks) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Towers> path = algorithm.getMethod().apply(numberOfDisks);
        log.info("{} algorithm found {} steps solution in {} milliseconds.",
                algorithm.getName(),
                path.size(),
                stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return path;
    }

    @Getter
    @RequiredArgsConstructor
    private enum Algorithm {
        BFS("Breadth First Search", Algorithms::iterativeBfs),
        DFS("Depth First Search", Algorithms::iterativeDfs),
        A_STAR("A*", Algorithms::aStar);

        private final String name;

        private final Function<Integer, List<Towers>> method;
    }
}
