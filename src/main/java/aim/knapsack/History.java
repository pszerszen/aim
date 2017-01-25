package aim.knapsack;

import lombok.Data;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Piotr
 */
@SuppressWarnings({ "MismatchedQueryAndUpdateOfCollection", "OptionalGetWithoutIsPresent" })
@Data
class History implements Comparable<History> {
    private final int iteration;

    private final Knapsack best;

    private final List<Knapsack> population;

    History(final int iteration, final Knapsack best) {
        this(iteration, best, Collections.emptyList());
    }

    History(final int iteration, final Knapsack best, final List<Knapsack> population) {
        this.iteration = iteration;
        this.best = best;
        this.population = population;
    }

    Triple<Integer, Integer, Integer> asTriple() {
        double averageValue = population.stream()
                .mapToInt(Knapsack::totalValue)
                .average()
                .orElse(0.0);
        return Triple.of(iteration, best.totalValue(), Double.valueOf(averageValue).intValue());
    }

    @Override
    public int compareTo(final History o) {
        return Integer.valueOf(iteration).compareTo(o.getIteration());
    }

    static List<Triple<Integer, Integer, Integer>> generateChartData(List<History> historyList){
        return historyList.stream()
                .map(History::asTriple)
                .sorted()
                .collect(Collectors.toList());
    }
}
