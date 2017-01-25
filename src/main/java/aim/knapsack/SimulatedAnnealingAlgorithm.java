package aim.knapsack;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.security.SecureRandom;

@Log4j2
public class SimulatedAnnealingAlgorithm extends AbstractAlgorithm {

    private SecureRandom random = new SecureRandom();

    @Getter
    private static final SimulatedAnnealingAlgorithm instance = new SimulatedAnnealingAlgorithm();

    @Override
    Knapsack solve() {

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

                if (mutant.totalValue() > current.totalValue() || random.nextInt() < acceptanceProbability) {
                    current = mutant.clone();
                    if (current.totalValue() > best.totalValue()) {
                        log.info("Best value changed from {} to {} on {} iteration while temperature is {}.", best.totalValue(), current.totalValue(), i, temperature);
                        best = current.clone();
                    }
                }
                i++;
                addToHistory(current);
            }
            temperature *= KnapsackConfig.getInstance().getCoolingRate();
        }
        finalValue = best;
        return best;
    }

    @Override
    String getOutputFileName() {
        return "simulatedAnnealingAlgorithm";
    }

    private static double acceptanceProbability(int averageValue, int newValue, int currentValue, double temperature) {
        return ((currentValue - newValue) / averageValue) * temperature;
    }
}
