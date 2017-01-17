package aim.knapsack;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KnapsackConfig {

    private final ImmutableList<Item> items;
    private final int maxTotalWeight;
    private final int numberOfIterations;

    // knapsack
    private final double initialTemperature = 10_000.0;
    private final double coolingRate = 0.003;

    public int totalItemsValue() {
        return items.stream()
                .mapToInt(Item::getValue)
                .sum();
    }
}
