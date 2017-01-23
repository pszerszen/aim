package aim.knapsack;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Algorithms {

    private static Knapsack simulatedAnnealing() {
        return SimulatedAnnealingAlgorithm.getInstance().solve();
    }

    private static Knapsack geneticAlgorithm() {
        return GeneticAlgorithm.getInstance().solve();
    }

    public static void main(String... args) {
        KnapsackConfig config = KnapsackConfig.getInstance();
        log.info(config);
        log.info("Total number of items: {}", config.getItems().size());
        log.info("Total value of items: {}", config.getItems().stream().mapToInt(Item::getValue).sum());
        log.info("Total weight of items: {}", config.getItems().stream().mapToInt(Item::getWeight).sum());

        log.info("\n\nSIMULATED ANNEALING:");
        Knapsack knapsack = Algorithms.simulatedAnnealing();
        log.info(knapsack.resultToString());
        log.info("Number of items in knapsack: {}", knapsack.stream().filter(Item::isInKnapsack).mapToInt(value -> 1).sum());
        log.info("Value of items in knapsack: {}", knapsack.totalValue());
        log.info("Weight of items in knapsack: {}", knapsack.totalWeight());
        log.info("Chart data: {}", History.generateChartData(SimulatedAnnealingAlgorithm.getInstance().getHistoryList()));
        KnapsackUtils.writeCsv(SimulatedAnnealingAlgorithm.getInstance());

        log.info("\n\nGENETIC ALGORITHM:");
        knapsack = Algorithms.geneticAlgorithm();
        log.info(knapsack.resultToString());
        log.info("Number of items in knapsack: {}", knapsack.stream().filter(Item::isInKnapsack).mapToInt(value -> 1).sum());
        log.info("Value of items in knapsack: {}", knapsack.totalValue());
        log.info("Weight of items in knapsack: {}", knapsack.totalWeight());
        log.info("Chart data: {}", History.generateChartData(GeneticAlgorithm.getInstance().getHistoryList()));
        KnapsackUtils.writeCsv(GeneticAlgorithm.getInstance());
    }
}
