package aim.knapsack;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Getter
@Builder
public class KnapsackConfig {

    @Getter
    private static final KnapsackConfig instance = init(KnapsackConfig.class.getResource("knapsack.properties").getPath());

    @SneakyThrows
    private static KnapsackConfig init(String filepath) {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get(filepath)));

        List<Item> items = new ArrayList<>();
        int i = 1;
        String value;
        while ((value = properties.getProperty(String.format("item%d", i))) != null) {
            items.add(toItem(value));
            i++;
        }

        int numberOfIterations = Integer.parseInt(properties.getProperty("iterations.number"));
        int maxTotalWeight = Integer.parseInt(properties.getProperty("max.total.weight"));

        return KnapsackConfig.builder()
                .items(ImmutableList.copyOf(items))
                .maxTotalWeight(maxTotalWeight)
                .numberOfIterations(numberOfIterations)
                .build();
    }

    private static Item toItem(String value) {
        String[] split = value.split("-");
        return Item.builder()
                .value(Integer.parseInt(split[ 0 ]))
                .weight(Integer.parseInt(split[ 1 ]))
                .build();
    }

    private final ImmutableList<Item> items;
    private final int maxTotalWeight;
    private final int numberOfIterations;

    // simulated annealing
    private final double initialTemperature = 10_000.0;
    private final double minimalTemperature = 0.000_01;
    private final double coolingRate = 0.003;

    // genetic
    private final int populationSize;
    private final double mutationsProbability = 0.1;
    private final double recombinationProbability;
    private final SelectionType selectionType = SelectionType.SimpleTournament;
    private final CrossingType crossingType = CrossingType.OnePoint;

    public int totalItemsValue() {
        return items.stream()
                .mapToInt(Item::getValue)
                .sum();
    }
}
