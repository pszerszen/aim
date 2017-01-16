package aim.knapsack;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Knapsack extends ArrayList<Item> {

    private final int maxTotalWeight;

    public Knapsack(int maxTotalWeight, List<Item> items) {
        this.maxTotalWeight = maxTotalWeight;
        items.forEach(this::add);
    }

    public Knapsack(int maxTotalWeight, Item... items) {
        this(maxTotalWeight, Arrays.asList(items));
    }

    public boolean isOverpacked() {
        return totalWeight() > maxTotalWeight;
    }

    public int totalWeight() {
        return stream()
                .filter(Item::isInKnapsack)
                .mapToInt(Item::getWeight)
                .sum();
    }

    public int totalValue() {
        return stream()
                .filter(Item::isInKnapsack)
                .mapToInt(Item::getValue)
                .sum();
    }

    public Knapsack getRandomValidNeighbour(int switchedItems) {
        Knapsack neighbour;
        do {
            neighbour = new Knapsack(maxTotalWeight, toArray(new Item[size()]));
            getRandomIndexes(switchedItems, size())
                    .forEach(i -> get(i).switchIsKnapsack());

        } while (KnapsackUtils.isValidNeighbour(this, neighbour));

        return neighbour;
    }

    private static Set<Integer> getRandomIndexes(int numberOftIndexes, int maxIndex) {
        Set<Integer> randoms = new HashSet<>(numberOftIndexes);
        do {
            randoms.add(RandomUtils.nextInt(0, maxIndex));
        } while (randoms.size() < numberOftIndexes);
        return randoms;
    }
}
