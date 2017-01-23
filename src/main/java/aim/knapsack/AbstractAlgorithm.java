package aim.knapsack;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAlgorithm {

    @Getter
    protected List<History> historyList = new ArrayList<>();

    abstract Knapsack solve();
}
