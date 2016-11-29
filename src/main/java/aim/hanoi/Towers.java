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
@EqualsAndHashCode(exclude = { "numberOfDisks" })
@ToString(exclude = { "numberOfDisks" })
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

    private final int numberOfDisks;

    static Towers initState(int numberOfDisks) {
        Towers towers = new Towers(numberOfDisks);
        towers.initState();
        return towers;
    }

    static Towers endState(int numberOfDisks) {
        Towers towers = new Towers(numberOfDisks);
        towers.endState();
        return towers;
    }

    private void initState() {
        putAllDisksOnTower(tower1);
    }

    private void endState() {
        putAllDisksOnTower(tower3);
    }

    private void putAllDisksOnTower(Tower tower) {
        for (int i = numberOfDisks; i >= 1; i--) {
            tower.push(i);
        }
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    protected Towers clone() throws CloneNotSupportedException {
        Towers clone = new Towers(numberOfDisks);

        clone.setTower1(tower1.clone());
        clone.setTower2(tower2.clone());
        clone.setTower3(tower3.clone());

        return clone;
    }

    int getFScore() {
        int tower1Value = tower1.stream()
                .mapToInt(Integer::intValue)
                .map(i -> i * WORST_TOWER_RATE)
                .sum();
        int tower2Value = tower2.stream()
                .mapToInt(Integer::intValue)
                .map(i -> i * MIDDLE_TOWER_RATE)
                .sum();
        int tower3Value = tower3.stream()
                .mapToInt(Integer::intValue)
                .map(i -> i * BEST_TOWER_RATE)
                .sum();

        return tower1Value + tower2Value + tower3Value;
    }

    int getGScore() {
        int tower1Value = tower1.stream()
                .mapToInt(Integer::intValue)
                .map(i -> i * BEST_TOWER_RATE)
                .sum();
        int tower2Value = tower2.stream()
                .mapToInt(Integer::intValue)
                .map(i -> i * MIDDLE_TOWER_RATE)
                .sum();
        int tower3Value = tower3.stream()
                .mapToInt(Integer::intValue)
                .map(i -> i * WORST_TOWER_RATE)
                .sum();

        return tower1Value + tower2Value + tower3Value;
    }

    Set<Towers> getPossibleSwitchStates() {
        return TowersUtils.getPossibleSwitchStates(this);
    }
}
