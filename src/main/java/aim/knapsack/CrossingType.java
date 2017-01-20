package aim.knapsack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.util.stream.IntStream;

@Getter
@RequiredArgsConstructor
enum CrossingType {
    OnePoint((parentA, parentB) -> {
        Knapsack child = Knapsack.empty(parentA);

        IntStream.range(0, child.size()).forEach(i -> {
            Knapsack predecessor = i < child.size() / 2 ? parentA : parentB;
            child.get(i).setInKnapsack(predecessor.get(0).isInKnapsack());
        });
        return child;
    }),
    TwoPoint((parentA, parentB) -> {
        Knapsack child = Knapsack.empty(parentA);
        int length = child.size();

        int firstSwapPoint = RandomUtils.nextInt(1, length / 2);
        int secondSwapPoint = RandomUtils.nextInt(length / 2 + 1, length - 1);

        IntStream.range(0, length).forEach(i -> {
            if (i < firstSwapPoint) {
                child.get(i).setInKnapsack(parentA.get(i).isInKnapsack());
            } else if (i < secondSwapPoint) {
                child.get(i).setInKnapsack(parentB.get(i).isInKnapsack());
            } else {
                child.get(i).setInKnapsack(parentA.get(i).isInKnapsack());
            }
        });
        return child;
    }),
    Uniform((parentA, parentB) -> {
        Knapsack child = Knapsack.empty(parentA);
        IntStream.range(0, parentA.size()).forEach(i -> {
            Knapsack predescessor = RandomUtils.nextBoolean() ? parentA : parentB;
            child.get(i).setInKnapsack(predescessor.get(i).isInKnapsack());
        });
        return child;
    });

    private final CrossingMethod method;

    private interface CrossingMethod {
        Knapsack method(Knapsack parentA, Knapsack parentB);
    }
}
