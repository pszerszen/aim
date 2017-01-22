package aim.knapsack;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class Item {

    private int weight;

    private int value;

    private boolean inKnapsack;

    void switchIsKnapsack() {
        setInKnapsack(!inKnapsack);
    }

    @Override
    protected Item clone() {
        return builder()
                .weight(weight)
                .value(value)
                .inKnapsack(inKnapsack)
                .build();
    }
}
