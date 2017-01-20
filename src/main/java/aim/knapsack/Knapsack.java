package aim.knapsack;

import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Knapsack extends ArrayList<Item> implements Comparable {

    @Getter
    private final int maxTotalWeight;

    public Knapsack(int maxTotalWeight, List<Item> items) {
        this.maxTotalWeight = maxTotalWeight;
        items.forEach(this::add);
    }

    public Knapsack(int maxTotalWeight, Item... items) {
        this(maxTotalWeight, Arrays.asList(items));
    }

    @Override
    public Knapsack clone() {
        return new Knapsack(maxTotalWeight, this);
    }

    /**
     * Packs elements until knapsack isn't overpacked.
     */
    public void initialState() {
        forEach(item -> item.setInKnapsack(false));

        for (final Item item : this) {
            if (wouldOverpack(item)) {
                break;
            }
            item.switchIsKnapsack();
        }
    }

    private boolean wouldOverpack(Item item) {
        return totalWeight() + item.getWeight() > maxTotalWeight;
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

    public Knapsack getRandomValidNeighbour() {
        Knapsack neighbour;
        int switchedItems = RandomUtils.nextInt(2, size() / 2);
        do {
            neighbour = new Knapsack(maxTotalWeight, toArray(new Item[ size() ]));
            getRandomIndexes(switchedItems, size())
                    .forEach(i -> get(i).switchIsKnapsack());

        } while (KnapsackUtils.isValidNeighbour(this, neighbour));

        return neighbour;
    }

    public static Knapsack empty(Knapsack knapsack){
        Knapsack emptyKnapsack = knapsack.clone();
        emptyKnapsack.forEach(item -> item.setInKnapsack(false));
        return emptyKnapsack;
    }

    public static Knapsack recalculateChild(Knapsack child){
        int weightSum = 0;
        for(Item item : child){
            if(item.isInKnapsack()){
                if(weightSum + item.getWeight() > child.maxTotalWeight){
                    item.setInKnapsack(false);
                } else {
                    weightSum += item.getWeight();
                }
            }
        }
        return child;
    }

    private static Set<Integer> getRandomIndexes(int numberOftIndexes, int maxIndex) {
        Set<Integer> randoms = new HashSet<>(numberOftIndexes);
        do {
            randoms.add(RandomUtils.nextInt(0, maxIndex));
        } while (randoms.size() < numberOftIndexes);
        return randoms;
    }

    @Override
    public int compareTo(final Object o) {
        return Integer.valueOf(totalValue()).compareTo(((Knapsack) o).totalValue());
    }
}
