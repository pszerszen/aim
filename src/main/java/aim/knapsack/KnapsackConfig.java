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

    public int totalItemsValue() {
        return items.stream()
                .mapToInt(Item::getValue)
                .sum();
    }
}
