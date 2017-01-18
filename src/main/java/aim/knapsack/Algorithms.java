package aim.knapsack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Algorithms {

    static Knapsack simulatedAnnealing2(KnapsackConfig config) {
        double temperature = config.getInitialTemperature();
        Knapsack current = new Knapsack(config.getMaxTotalWeight(), config.getItems());
        current.initialState();
        Knapsack best = current.clone();
        int averageValue = current.totalValue();

        log.info("Temperature: {}", temperature);
        while (temperature > config.getMinimalTemperature()) {
            int i = 0;
            while (i < config.getNumberOfIterations()) {
                Knapsack mutant = current.getRandomValidNeighbour();
                double acceptanceProbability = acceptanceProbabilty(averageValue, mutant.totalValue(), current.totalValue(), temperature);

                if (mutant.totalValue() > current.totalValue() || acceptanceProbability > Math.random()) {
                    current = mutant.clone();
                    if (current.totalValue() > best.totalValue()) {
                        best = current.clone();
                    }
                }
                i++;
            }
            temperature *= config.getCoolingRate();
        }
        return best;
    }

    private static double acceptanceProbabilty(int averageValue, int newValue, int currentValue, double temperature) {
        return ((currentValue - newValue) / averageValue) * temperature;
    }

    @Deprecated
    static Knapsack simulatedAnnealing(KnapsackConfig config) {
        double temperature = config.getInitialTemperature();
        Knapsack knapsack = new Knapsack(config.getMaxTotalWeight(), config.getItems());
        knapsack.initialState();
        Knapsack best = knapsack.clone();

        int i = 0;
        while (i < config.getNumberOfIterations()) {
            int value = knapsack.totalValue();
            Knapsack mutant = knapsack.getRandomValidNeighbour();
            int newValue = mutant.totalValue();

            if (acceptanceProbability(value, newValue, temperature) > Math.random()) {
                knapsack = mutant;
            }
            if (knapsack.totalValue() > best.totalValue()) {
                best = knapsack.clone();
            }

            temperature *= config.getCoolingRate();
            i++;
        }
        return knapsack;
    }

    private static double acceptanceProbability(int value, int newValue, double temperature) {
        return newValue > value ?
                1.0 :
                Math.exp((newValue - value) / temperature);
    }
}
