package aim.hanoi;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class Towers {

    private static final int WORST_TOWER_RATE = 2;

    private static final int MIDDLE_TOWER_RATE = 1;

    private static final int BEST_TOWER_RATE = 0;

    static Comparator<Towers> F_SCORE_COMPARATOR = Comparator.comparing(Towers::getFScore);

    @Getter
    @Setter
    private Tower tower1 = new Tower();

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Getter
    @Setter
    private Tower tower2 = new Tower();

    @Getter
    @Setter
    private Tower tower3 = new Tower();

    static Towers initState(int numberOfDisks) {
        Towers towers = new Towers();
        putAllDisksOnTower(numberOfDisks, towers.tower1);
        return towers;
    }

    static Towers endState(int numberOfDisks) {
        Towers towers = new Towers();
        putAllDisksOnTower(numberOfDisks, towers.tower3);
        return towers;
    }

    private static void putAllDisksOnTower(int numberOfDisks, Tower tower) {
        for (int i = numberOfDisks; i >= 1; i--) {
            tower.push(i);
        }
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    protected Towers clone() throws CloneNotSupportedException {
        Towers clone = new Towers();

        clone.setTower1(tower1.clone());
        clone.setTower2(tower2.clone());
        clone.setTower3(tower3.clone());

        return clone;
    }

    /**
     * In theory: the total cost of getting from the start node to the goal by passing by that node.
     * In practise: the total cost of getting from that node to the goal.
     */
    int getFScore() {
        int tower1Value = tower1.stream()
                .mapToInt(Integer::intValue)
                .sum() * WORST_TOWER_RATE;
        int tower2Value = tower2.stream()
                .mapToInt(Integer::intValue)
                .sum() * MIDDLE_TOWER_RATE;
        int tower3Value = tower3.stream()
                .mapToInt(Integer::intValue)
                .sum() * BEST_TOWER_RATE;

        return tower1Value + tower2Value + tower3Value;
    }

    /**
     * Cost of getting from the start node to that node.
     */
    int getGScore() {
        int tower1Value = tower1.stream()
                .mapToInt(Integer::intValue)
                .sum() * BEST_TOWER_RATE;
        int tower2Value = tower2.stream()
                .mapToInt(Integer::intValue)
                .sum() * MIDDLE_TOWER_RATE;
        int tower3Value = tower3.stream()
                .mapToInt(Integer::intValue)
                .sum() * WORST_TOWER_RATE;

        return tower1Value + tower2Value + tower3Value;
    }

    Set<Towers> getPossibleSwitchStates() {
        return TowersUtils.getPossibleSwitchStates(this);
    }
}
