package aim.knapsack;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Knapsack extends ArrayList<Item> implements Comparable<Knapsack> {

    private Knapsack(List<Item> items) {
        items.forEach(this::add);
    }

    private Knapsack() {
        super();
    }

    static Knapsack withInitialState() {
        Knapsack knapsack = new Knapsack(
                KnapsackConfig.getInstance().getItems());
        knapsack.initialState();
        return knapsack;
    }

    static Knapsack empty(Knapsack knapsack) {
        Knapsack emptyKnapsack = knapsack.clone();
        emptyKnapsack.forEach(item -> item.setInKnapsack(false));
        return emptyKnapsack;
    }

    @Override
    public Knapsack clone() {
        Knapsack clone = new Knapsack();
        forEach(item -> clone.add(item.clone()));
        return clone;
    }

    /**
     * Packs elements until knapsack isn't overpacked.
     */
    private void initialState() {
        forEach(item -> item.setInKnapsack(false));

        for (final Item item : this) {
            if (wouldOverpack(item)) {
                break;
            }
            item.switchIsKnapsack();
        }
    }

    private boolean wouldOverpack(Item item) {
        return totalWeight() + item.getWeight() > KnapsackConfig.getInstance().getMaxTotalWeight();
    }

    boolean isOverpacked() {
        return totalWeight() > KnapsackConfig.getInstance().getMaxTotalWeight();
    }

    int totalWeight() {
        return stream()
                .filter(Item::isInKnapsack)
                .mapToInt(Item::getWeight)
                .sum();
    }

    int totalValue() {
        return stream()
                .filter(Item::isInKnapsack)
                .mapToInt(Item::getValue)
                .sum();
    }

    Knapsack getRandomValidNeighbour() {
        Knapsack neighbour;
        do {
            neighbour = getRandomNeighbour();
        } while (!KnapsackUtils.isValidNeighbour(this, neighbour));
        return neighbour;
    }

    private Knapsack getRandomNeighbour() {
        Knapsack neighbour = clone();
        int switchedItems = RandomUtils.nextInt(2, size() / 2);
        for (int i : KnapsackUtils.getRandomIndexes(switchedItems, size())) {
            neighbour.get(i).switchIsKnapsack();
        }
        return neighbour;
    }

    String resultToString() {
        return stream()
                .filter(Item::isInKnapsack)
                .map(this::toString)
                .collect(Collectors.joining(", ", "Knapsack=[", "]"));
    }

    private String toString(Item item) {
        return String.format("Item(value=%s, weight=%s)", item.getValue(), item.getWeight());
    }

    @Override
    public int compareTo(final Knapsack o) {
        return Integer.valueOf(totalValue()).compareTo(o.totalValue());
    }
}
