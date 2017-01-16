package aim.knapsack;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KnapsackUtils {

    @SneakyThrows
    public static KnapsackConfig loadInputFile(String filepath) {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get(filepath)));

        int numberItems = Integer.parseInt(properties.getProperty("items.number"));
        List<Item> items = IntStream.rangeClosed(1, numberItems)
                .mapToObj(i -> String.format("item%d", i))
                .map(properties::getProperty)
                .map(KnapsackUtils::toItem)
                .collect(Collectors.toList());

        int numberOfIterations = Integer.parseInt(properties.getProperty("iterations.number"));
        int maxTotalWeight = Integer.parseInt(properties.getProperty("max.total.weight"));

        return KnapsackConfig.builder()
                .items(ImmutableList.copyOf(items))
                .maxTotalWeight(maxTotalWeight)
                .numberOfIterations(numberOfIterations)
                .build();
    }

    public static boolean isValid(Knapsack knapsack){
        return !(knapsack == null || knapsack.isOverpacked());
    }

    public static boolean isValidNeighbour(Knapsack knapsack, Knapsack neighbour){
        return isValid(neighbour) && !knapsack.equals(neighbour);
    }

    private static Item toItem(String value) {
        String[] split = value.split("-");
        return Item.builder()
                .value(Integer.parseInt(split[ 0 ]))
                .weight(Integer.parseInt(split[ 1 ]))
                .build();
    }
}
