package aim.knapsack;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GeneticAlgorithm extends AbstractAlgorithm {

    @Getter
    private static final GeneticAlgorithm instance = new GeneticAlgorithm();
    private final SecureRandom random = new SecureRandom(RandomUtils.nextBytes(1024));

    private List<Double> roulette = new ArrayList<>();

    private Knapsack best;

    private int uselessGenerations = 0;
    private int betterGenerations = 0;

    @Override
    Knapsack solve() {
        Knapsack initiator = Knapsack.withInitialState();
        List<Knapsack> population = generateFirstGeneration(initiator);

        best = initiator.clone();

        while (uselessGenerations < KnapsackConfig.getInstance().getNumberOfIterations()) {
            addToHistory(best.clone(), new ArrayList<>(population));
            List<Knapsack> newPopulation = new ArrayList<>();
            int latPopulationBest = best.totalValue();

            for (int i = 0; i < KnapsackConfig.getInstance().getPopulationSize() / 2; i++) {
                generateChildren(population, newPopulation);
            }

            population = new ArrayList<>(newPopulation);
            if (best.totalValue() > latPopulationBest) {
                uselessGenerations = 0;
                betterGenerations++;
            } else {
                uselessGenerations++;
            }

            roulette.clear();
        }

        finalValue = best;
        return best;
    }

    @Override
    String getOutputFileName() {
        return "geneticAlgorithm";
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

        if (random.nextDouble() <= KnapsackConfig.getInstance().getRecombinationProbability()) {
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
            log.info("Best value changed from {} to {} after {} useless generations and {} better generations.",
                    best.totalValue(),
                    child.totalValue(),
                    uselessGenerations,
                    betterGenerations);
            best = child.clone();
        }
        return child;
    }

    private Knapsack mutateChild(Knapsack child) {
        Knapsack mutant = child.clone();
        for (Item item : mutant) {
            if (random.nextDouble() < KnapsackConfig.getInstance().getMutationsProbability()) {
                item.switchIsKnapsack();
            }
        }
        return mutant;
    }

    private Knapsack chooseParent(List<Knapsack> population) {
        return KnapsackConfig.getInstance().getSelectionType().select(population, roulette);
    }

}
