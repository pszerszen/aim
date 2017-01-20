package aim.knapsack;

import lombok.extern.log4j.Log4j2;

@Log4j2
public final class Algorithms {

    private static final GeneticAlgorithm GENETIC_ALGORITHM = new GeneticAlgorithm();

    Knapsack simulatedAnnealing() {
        double temperature = KnapsackConfig.getInstance().getInitialTemperature();
        Knapsack current = new Knapsack(KnapsackConfig.getInstance().getMaxTotalWeight(), KnapsackConfig.getInstance().getItems());
        current.initialState();
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
}
