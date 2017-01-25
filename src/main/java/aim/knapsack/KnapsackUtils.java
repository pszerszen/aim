package aim.knapsack;

import com.opencsv.CSVWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class KnapsackUtils {

    private static SecureRandom random = new SecureRandom();

    private static boolean isValid(Knapsack knapsack) {
        return !(knapsack == null || knapsack.isOverpacked());
    }

    static boolean isValidNeighbour(Knapsack knapsack, Knapsack neighbour) {
        return isValid(neighbour) && !knapsack.equals(neighbour);
    }

    static Knapsack eliminateOverload(Knapsack child) {
        Knapsack fixed = child.clone();
        int weightSum = 0;
        for (Item item : fixed) {
            if (item.isInKnapsack()) {
                if (weightSum + item.getWeight() > KnapsackConfig.getInstance().getMaxTotalWeight()) {
                    item.setInKnapsack(false);
                } else {
                    weightSum += item.getWeight();
                }
            }
        }
        return fixed;
    }

    static Set<Integer> getRandomIndexes(int numberOftIndexes, int maxIndex) {
        Set<Integer> randoms = new HashSet<>(numberOftIndexes);
        do {
            randoms.add(random.nextInt(maxIndex));
        } while (randoms.size() < numberOftIndexes);
        return randoms;
    }

    @SneakyThrows
    static void writeCsv(AbstractAlgorithm algorithm) {
        try(CSVWriter writer = new CSVWriter(new FileWriter(algorithm.getOutputFileName() + ".csv"), ';')) {
            writer.writeNext(line("Number of all elements", KnapsackConfig.getInstance().getItems().size()));
            writer.writeNext(line());
            writer.writeNext(line("Knapsack elements:"));
            writer.writeNext(line("element value", "element weight"));
            KnapsackConfig.getInstance().getItems().stream()
                    .map(item -> new Object[] { item.getValue(), item.getWeight() })
                    .map(KnapsackUtils::line)
                    .forEachOrdered(writer::writeNext);
            writer.writeNext(line());
            List<History> history = algorithm.getHistoryList();
            writer.writeNext(line("Iterations:", history.size()));
            writer.writeNext(line("Iteration number", "best unit's value", "population average value"));
            algorithm.getHistoryList().stream()
                    .map(History::asTriple)
                    .map(triple -> new Object[] { triple.getLeft(), triple.getMiddle(), triple.getRight() })
                    .map(KnapsackUtils::line)
                    .forEachOrdered(writer::writeNext);
            writer.writeNext(line("Final knapsack:"));
            writer.writeNext(line("element value", "element weight"));
            algorithm.getFinalValue().stream()
                    .filter(Item::isInKnapsack)
                    .map(item -> new Object[] { item.getValue(), item.getWeight() })
                    .map(KnapsackUtils::line)
                    .forEachOrdered(writer::writeNext);
        }
    }

    private static String[] line(Object... cells) {
        String[] line = new String[ cells.length ];
        IntStream.range(0, cells.length).forEach(i -> line[ i ] = cells[ i ].toString());
        return line;
    }
}
