package aim.knapsack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Algorithms {

    static Knapsack simulatedAnnealing(KnapsackConfig config) {
        Knapsack knapsack = new Knapsack(config.getMaxTotalWeight(), config.getItems());
        knapsack.initialState();
        Knapsack best = knapsack.clone();
        double temperature = config.getInitialTemperature();

        for (int i = 0; i < config.getNumberOfIterations(); i++) {
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
        }
        return knapsack;
    }

    public static double acceptanceProbability(int value, int newValue, double temperature) {
        return newValue > value ?
                1.0 :
                Math.exp((newValue - value) / temperature);
    }
}
