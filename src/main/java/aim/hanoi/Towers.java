package aim.hanoi;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"numberOfDisks"})
@ToString(exclude = {"numberOfDisks"})
public class Towers {

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

    static Towers initState(int numberOfDisks){
        Towers towers = new Towers(numberOfDisks);
        towers.initState();
        return towers;
    }

    static Towers endState(int numberOfDisks){
        Towers towers = new Towers(numberOfDisks);
        towers.endState();
        return towers;
    }

    private void initState() {
        for (int i = numberOfDisks; i >= 1; i--) {
            tower1.push(i);
        }
    }

    private void endState(){
        for (int i = numberOfDisks; i >= 1; i--) {
            tower3.push(i);
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

    int getFScore(){
        BigInteger fScore = BigInteger.ZERO;

        tower1.forEach(disk -> fScore.add(BigInteger.valueOf(disk * 2)));
        tower2.forEach(disk -> fScore.add(BigInteger.valueOf(disk)));

        return fScore.intValue();
    }

    int getGScore(){
        BigInteger fScore = BigInteger.ZERO;

        tower2.forEach(disk -> fScore.add(BigInteger.valueOf(disk)));
        tower3.forEach(disk -> fScore.add(BigInteger.valueOf(disk * 2)));

        return fScore.intValue();
    }

    Set<Towers> getPossibleSwitchStates() {
        return TowersUtils.getPossibleSwitchStates(this);
    }
}
