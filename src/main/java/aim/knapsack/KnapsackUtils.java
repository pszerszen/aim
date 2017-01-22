package aim.knapsack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class KnapsackUtils {

    private static boolean isValid(Knapsack knapsack) {
        return !(knapsack == null || knapsack.isOverpacked());
    }

    static boolean isValidNeighbour(Knapsack knapsack, Knapsack neighbour) {
        return isValid(neighbour) && !knapsack.equals(neighbour);
    }

    static Knapsack eliminateOverload(Knapsack child) {
        Knapsack fixed = child.clone();
        int weightSum = 0;
        for (Item item : fixed) {
            if (item.isInKnapsack()) {
                if (weightSum + item.getWeight() > KnapsackConfig.getInstance().getMaxTotalWeight()) {
                    item.setInKnapsack(false);
                } else {
                    weightSum += item.getWeight();
                }
            }
        }
        return fixed;
    }

    static Set<Integer> getRandomIndexes(int numberOftIndexes, int maxIndex) {
        Set<Integer> randoms = new HashSet<>(numberOftIndexes);
        do {
            randoms.add(RandomUtils.nextInt(0, maxIndex));
        } while (randoms.size() < numberOftIndexes);
        return randoms;
    }
}
