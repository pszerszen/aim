package aim.knapsack;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
enum SelectionType {
    /**
     * Get the best unit out of randomly picked half of population.
     */
    SimpleTournament((population, roulette) -> {
        return IntStream.range(0, population.size() / 2)
                .map(i -> RandomUtils.nextInt(0, population.size()))
                .distinct().boxed()
                .map(population::get)
                .max(Knapsack::compareTo).get();
    }),
    Roulette((population, roulette) -> {
        if (CollectionUtils.isEmpty(roulette)) {
            roulette.addAll(createRoulette(population));
        }

        double random = Math.random();
        return IntStream.range(0, roulette.size()).boxed()
                .filter(i -> random < roulette.get(i))
                .map(population::get)
                .findFirst()
                .orElse(population.get(0));
    });

    private final SelectionTypeMethod method;

    Knapsack select(List<Knapsack> population, List<Double> roulette) {
        return method.select(population, roulette);
    }

    private static List<Double> createRoulette(List<Knapsack> population) {
        int populationTotalFitness = population.stream()
                .mapToInt(Knapsack::totalValue)
                .sum();

        List<Double> probabilities = new ArrayList<>();
        double currentTotal = 0.0;
        for (Knapsack knapsack : population) {
            currentTotal += knapsack.totalValue() / populationTotalFitness;
            probabilities.add(currentTotal);
        }
        return probabilities;
    }

    private interface SelectionTypeMethod {
        Knapsack select(List<Knapsack> population, List<Double> roulette);
    }
}
