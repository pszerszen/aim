package aim.knapsack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

@Log4j2
@RequiredArgsConstructor
public class GeneticAlgorithm {

    @Getter
    private static final GeneticAlgorithm instance = new GeneticAlgorithm();

    private List<Double> roulette;

    private Knapsack best;


    private Knapsack onChildPush(Knapsack child) {
        child = mutateChild(child);
        child = Knapsack.recalculateChild(child);

        if(child.totalValue() > best.totalValue()){
            log.info("{} -> {}", best.totalValue(), child.totalValue());
            best = child.clone();
        }
        return child;
    }

    private Knapsack mutateChild(Knapsack child) {
        for (Item item : child) {
            if (Math.random() > KnapsackConfig.getInstance().getMutationsProbability()) {
                item.switchIsKnapsack();
            }
        }
        return child;
    }

    private Knapsack chooseParent(List<Knapsack> population) {
        Function<List<Knapsack>, Knapsack> selectionTypeMethod = KnapsackConfig.getInstance().getSelectionType() == SelectionType.SimpleTournament ?
                this::simpleTournament :
                this::simpleRoulette;
        return selectionTypeMethod.apply(population);
    }

    @SuppressWarnings("unchecked")
    private Knapsack simpleTournament(List<Knapsack> population) {
        int parent1 = RandomUtils.nextInt(0, population.size());
        int parent2 = RandomUtils.nextInt(0, population.size());

        return ObjectUtils.max(population.get(parent1), population.get(parent2));
    }

    private Knapsack simpleRoulette(List<Knapsack> population) {
        if (CollectionUtils.isEmpty(roulette)) {
            roulette = createRoulette(population);
        }

        double random = Math.random();
        return IntStream.range(0, roulette.size()).boxed()
                .filter(i -> random < roulette.get(i))
                .map(population::get)
                .findFirst()
                .orElse(population.get(0));
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

}
