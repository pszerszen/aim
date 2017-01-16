package aim.knapsack;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

    private int weight;
    private int value;
    private boolean inKnapsack;

    public void switchIsKnapsack(){
        inKnapsack = !inKnapsack;
    }
}
