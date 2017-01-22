package aim.knapsack;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Algorithms {

    static Knapsack simulatedAnnealing() {
        double temperature = KnapsackConfig.getInstance().getInitialTemperature();
        Knapsack current = Knapsack.withInitialState();
        Knapsack best = current.clone();
        int averageValue = current.totalValue();

        log.info("Temperature: {}", temperature);
        while (temperature > KnapsackConfig.getInstance().getMinimalTemperature()) {
            int i = 0;
            while (i < KnapsackConfig.getInstance().getNumberOfIterations()) {
                Knapsack mutant = current.getRandomValidNeighbour();
                double acceptanceProbability = acceptanceProbability(averageValue, mutant.totalValue(), current.totalValue(), temperature);

                if (mutant.totalValue() > current.totalValue() || acceptanceProbability > Math.random()) {
                    current = mutant.clone();
                    if (current.totalValue() > best.totalValue()) {
                        best = current.clone();
                    }
                }
                i++;
            }
            temperature *= KnapsackConfig.getInstance().getCoolingRate();
        }
        return best;
    }

    private static double acceptanceProbability(int averageValue, int newValue, int currentValue, double temperature) {
        return ((currentValue - newValue) / averageValue) * temperature;
    }

    static Knapsack geneticAlgorithm() {
        return GeneticAlgorithm.getInstance().solve();
    }

    public static void main(String... args) {
        KnapsackConfig config = KnapsackConfig.getInstance();
        log.info(config);
        log.info("Total number of items: {}", config.getItems().size());
        log.info("Total value of items: {}", config.getItems().stream().mapToInt(Item::getValue).sum());
        log.info("Total weight of items: {}", config.getItems().stream().mapToInt(Item::getWeight).sum());

        Knapsack knapsack = Algorithms.simulatedAnnealing();
        log.info(knapsack.resultToString());
        log.info("Number of items in knapsack: {}", knapsack.stream().filter(Item::isInKnapsack).mapToInt(value -> 1).sum());
        log.info("Value of items in knapsack: {}", knapsack.totalValue());
        log.info("Weight of items in knapsack: {}", knapsack.totalWeight());

        knapsack = Algorithms.geneticAlgorithm();
        log.info(knapsack.resultToString());
        log.info("Number of items in knapsack: {}", knapsack.stream().filter(Item::isInKnapsack).mapToInt(value -> 1).sum());
        log.info("Value of items in knapsack: {}", knapsack.totalValue());
        log.info("Weight of items in knapsack: {}", knapsack.totalWeight());
    }
}
