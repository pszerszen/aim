package aim.knapsack;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KnapsackUtils {

    @SneakyThrows
    public static KnapsackConfig loadInputFile(String filepath) {
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

    private static boolean isValid(Knapsack knapsack) {
        return !(knapsack == null || knapsack.isOverpacked());
    }

    static boolean isValidNeighbour(Knapsack knapsack, Knapsack neighbour) {
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
