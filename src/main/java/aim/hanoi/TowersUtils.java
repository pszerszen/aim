package aim.hanoi;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class TowersUtils {

    private static final String BASE_FILE_PATH = String.join(File.separator,
            System.getProperty("user.home"),
            "workspace", "aim", "src", "main", "resources", "aim", "hanoi")
            .concat(File.separator);

    static final String DISKS = "d";

    static final String HELP = "h";

    static final Options OPTIONS = new Options()
                    .addOption(new Option(DISKS, "disks", true, "Number of disks."))
                    .addOption(new Option(HELP, "help", false, "Print this message."));

    @SuppressWarnings("unused")
    static Map<Towers, Set<Towers>> generateGraphMap(int numberOfDisks) {
        Map<Towers, Set<Towers>> states = Maps.newHashMap();
        addStates(states, Towers.initState(numberOfDisks));

        return states;
    }

    private static void addStates(Map<Towers, Set<Towers>> states, Towers towers) {
        if (!states.containsKey(towers)) {
            Set<Towers> possibleSwitchStates = getPossibleSwitchStates(towers);
            states.put(towers, possibleSwitchStates);
            possibleSwitchStates.stream()
                    .filter(possibleSwitchStatus -> !states.containsKey(possibleSwitchStatus))
                    .forEach(possibleSwitchState -> addStates(states, possibleSwitchState));
        }
    }

    @SneakyThrows
    static Set<Towers> getPossibleSwitchStates(Towers towers) {
        ImmutableSet.Builder<Towers> statesBuilder = ImmutableSet.builder();

        if (canMoveDisk(towers.getTower1(), towers.getTower2())) {
            Towers newState = towers.clone();
            newState.getTower2().push(newState.getTower1().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower1(), towers.getTower3())) {
            Towers newState = towers.clone();
            newState.getTower3().push(newState.getTower1().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower2(), towers.getTower1())) {
            Towers newState = towers.clone();
            newState.getTower1().push(newState.getTower2().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower2(), towers.getTower3())) {
            Towers newState = towers.clone();
            newState.getTower3().push(newState.getTower2().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower3(), towers.getTower1())) {
            Towers newState = towers.clone();
            newState.getTower1().push(newState.getTower3().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower3(), towers.getTower2())) {
            Towers newState = towers.clone();
            newState.getTower2().push(newState.getTower3().pop());
            statesBuilder.add(newState);
        }

        return statesBuilder.build();
    }

    private static boolean canMoveDisk(Tower from, Tower to) {
        return !from.isEmpty() && to.canPush(from.peek());
    }

    @SneakyThrows
    static void saveResultsToFile(List<Towers> path, File file, int numberOfDisks) {
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static File getFile(String name) {
        new File(BASE_FILE_PATH).mkdirs();
        return new File(BASE_FILE_PATH + name + ".txt");
    }

    static void printHelpAndExit(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(Hanoi.class.getSimpleName(), options);
        System.exit(0);
    }
}
