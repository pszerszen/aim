package aim.knapsack;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class Knapsack extends ArrayList<Item> implements Comparable<Knapsack> {

    private Knapsack(List<Item> items) {
        items.forEach(this::add);
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
        return (Knapsack) super.clone();
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

    private int totalWeight() {
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
        int switchedItems = RandomUtils.nextInt(2, size() / 2);
        do {
            neighbour = clone();
            KnapsackUtils.getRandomIndexes(switchedItems, size())
                    .forEach(i -> get(i).switchIsKnapsack());

        } while (KnapsackUtils.isValidNeighbour(this, neighbour));

        return neighbour;
    }

    @Override
    public int compareTo(final Knapsack o) {
        return Integer.valueOf(totalValue()).compareTo(o.totalValue());
    }
}
