package aim.knapsack;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SimulatedAnnealingAlgorithm extends AbstractAlgorithm {

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

                if (mutant.totalValue() > current.totalValue() || acceptanceProbability > Math.random()) {
                    current = mutant.clone();
                    if (current.totalValue() > best.totalValue()) {
                        best = current.clone();
                    }
                }
                i++;
                historyList.add(new History(i, current));
            }
            temperature *= KnapsackConfig.getInstance().getCoolingRate();
        }
        return best;
    }

    private static double acceptanceProbability(int averageValue, int newValue, int currentValue, double temperature) {
        return ((currentValue - newValue) / averageValue) * temperature;
    }
}
