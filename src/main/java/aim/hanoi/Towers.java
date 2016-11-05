package aim.hanoi;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.Set;

@Data
@Log4j2
public class Towers {

    private Tower tower1 = new Tower();

    private Tower tower2 = new Tower();

    private Tower tower3 = new Tower();

    private final int numberOfDisks;

    private void initState() {
        for (int i = numberOfDisks; i >= 1; i--) {
            tower1.push(i);
        }
    }

    @Override
    protected Towers clone() throws CloneNotSupportedException {
        Towers clone = new Towers(numberOfDisks);

        clone.setTower1(tower1.clone());
        clone.setTower2(tower2.clone());
        clone.setTower3(tower3.clone());

        return clone;
    }

    public static Map<Towers, Set<Towers>> generateGraph(int numberOfDisks) {
        Towers towers = new Towers(numberOfDisks);
        towers.initState();

        Map<Towers, Set<Towers>> states = Maps.newHashMap();
        addStates(states, towers);

        return states;
    }

    private static void addStates(Map<Towers, Set<Towers>> states, Towers towers) {
        if (!states.containsKey(towers)) {
            Set<Towers> possibleSwitchStates = getPossibleSwitchStates(towers);
            states.put(towers, possibleSwitchStates);
            possibleSwitchStates.stream()
                    .filter(possibleSwitchStatus -> !states.containsKey(possibleSwitchStatus))
                    .forEach(possibleSwitchState -> addStates(states, possibleSwitchState));
        }
    }

    @SneakyThrows
    private static Set<Towers> getPossibleSwitchStates(Towers towers) {
        Builder<Towers> statesBuilder = ImmutableSet.builder();

        if (canMoveDisk(towers.getTower1(), towers.getTower2())) {
            Towers newState = towers.clone();
            newState.getTower2().push(newState.getTower1().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower1(), towers.getTower3())) {
            Towers newState = towers.clone();
            newState.getTower3().push(newState.getTower1().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower2(), towers.getTower1())) {
            Towers newState = towers.clone();
            newState.getTower1().push(newState.getTower2().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower2(), towers.getTower3())) {
            Towers newState = towers.clone();
            newState.getTower3().push(newState.getTower2().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower3(), towers.getTower1())) {
            Towers newState = towers.clone();
            newState.getTower1().push(newState.getTower3().pop());
            statesBuilder.add(newState);
        }

        if (canMoveDisk(towers.getTower3(), towers.getTower2())) {
            Towers newState = towers.clone();
            newState.getTower2().push(newState.getTower3().pop());
            statesBuilder.add(newState);
        }

        return statesBuilder.build();
    }

    private static boolean canMoveDisk(Tower from, Tower to) {
        return !from.isEmpty() && to.canPush(from.peek());
    }

    public static void main(String[] args) {
        Map<Towers, Set<Towers>> towersSetMap = Towers.generateGraph(6);

        log.error(towersSetMap);
    }
}
