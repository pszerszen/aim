package aim.knapsack;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GeneticAlgorithm {

    @Getter
    private static final GeneticAlgorithm instance = new GeneticAlgorithm();

    private List<Double> roulette = new ArrayList<>();

    private List<History> historyList = new ArrayList<>();

    private Knapsack best;

    Knapsack solve() {
        Knapsack initiator = Knapsack.withInitialState();
        List<Knapsack> population = generateFirstGeneration(initiator);

        best = initiator.clone();
        int uselessGenerations = 0;
        int iteration = 0;

        while (uselessGenerations < KnapsackConfig.getInstance().getNumberOfIterations()) {
            iteration++;
            historyList.add(new History(iteration, best, population));
            List<Knapsack> newPopulation = new ArrayList<>();
            int latPopulationBest = best.totalValue();

            for (int i = 0; i < KnapsackConfig.getInstance().getPopulationSize() / 2; i++) {
                generateChildren(population, newPopulation);
            }

            population = new ArrayList<>(newPopulation);
            if (best.totalValue() > latPopulationBest) {
                uselessGenerations = 0;
            } else {
                uselessGenerations++;
            }

            roulette.clear();
        }

        return best;
    }

    private List<Knapsack> generateFirstGeneration(Knapsack initiator) {
        return IntStream.range(0, KnapsackConfig.getInstance().getPopulationSize())
                .boxed()
                .map(operand -> initiator.getRandomValidNeighbour())
                .collect(Collectors.toList());
    }

    private void generateChildren(List<Knapsack> population, List<Knapsack> newPopulation) {
        Knapsack parentA = chooseParent(population);
        Knapsack parentB = chooseParent(population);

        if (Math.random() <= KnapsackConfig.getInstance().getRecombinationProbability()) {
            newPopulation.add(onChildPush(crossParents(parentA, parentB)));
            newPopulation.add(onChildPush(crossParents(parentB, parentA)));
        } else {
            newPopulation.add(onChildPush(parentA));
            newPopulation.add(onChildPush(parentB));
        }
    }

    private Knapsack crossParents(Knapsack parent1, Knapsack parent2) {
        return KnapsackConfig.getInstance().getCrossingType()
                .cross(parent1, parent2);
    }

    private Knapsack onChildPush(Knapsack child) {
        child = mutateChild(child);
        child = KnapsackUtils.eliminateOverload(child);

        if (child.totalValue() > best.totalValue()) {
            log.info("{} -> {}", best.totalValue(), child.totalValue());
            best = child.clone();
        }
        return child;
    }

    private Knapsack mutateChild(Knapsack child) {
        Knapsack mutant = child.clone();
        for (Item item : mutant) {
            if (Math.random() <= KnapsackConfig.getInstance().getMutationsProbability()) {
                item.switchIsKnapsack();
            }
        }
        return mutant;
    }

    List<Triple<Integer, Integer, Double>> getChartData() {
        return historyList.stream()
                .map(History::asTriple)
                .sorted()
                .collect(Collectors.toList());
    }

    private Knapsack chooseParent(List<Knapsack> population) {
        return KnapsackConfig.getInstance().getSelectionType().select(population, roulette);
    }

    @SuppressWarnings({ "MismatchedQueryAndUpdateOfCollection", "OptionalGetWithoutIsPresent" })
    @Data
    private class History implements Comparable<History> {
        private final int iteration;

        private final Knapsack best;

        private final List<Knapsack> population;

        Triple<Integer, Integer, Double> asTriple() {
            double averageValue = population.stream()
                    .mapToInt(Knapsack::totalValue)
                    .average().getAsDouble();
            return Triple.of(iteration, best.totalValue(), averageValue);
        }

        @Override
        public int compareTo(final History o) {
            return Integer.valueOf(iteration).compareTo(o.getIteration());
        }
    }
}
