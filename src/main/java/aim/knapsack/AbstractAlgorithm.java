package aim.knapsack;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractAlgorithm {

    @Getter
    private List<History> historyList = new ArrayList<>();

    @Getter
    protected Knapsack finalValue;

    protected void addToHistory(Knapsack best) {
        historyList.add(new History(historyList.size() + 1, best));
    }

    protected void addToHistory(Knapsack best, List<Knapsack> population) {
        historyList.add(new History(historyList.size() + 1, best, population));
    }

    protected void addToHistory(Knapsack best, Knapsack current){
        addToHistory(best, Collections.singletonList(current));
    }

    abstract Knapsack solve();

    abstract String getOutputFileName();
}
