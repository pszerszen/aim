package aim.knapsack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KnapsackUtils {

    private static boolean isValid(Knapsack knapsack) {
        return !(knapsack == null || knapsack.isOverpacked());
    }

    static boolean isValidNeighbour(Knapsack knapsack, Knapsack neighbour) {
        return isValid(neighbour) && !knapsack.equals(neighbour);
    }
}
