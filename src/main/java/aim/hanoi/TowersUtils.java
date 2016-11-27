package aim.hanoi;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Set;

public class TowersUtils {

    public static Map<Towers, Set<Towers>> generateGraphMap(int numberOfDisks) {
        Map<Towers, Set<Towers>> states = Maps.newHashMap();
        addStates(states, Towers.initState(numberOfDisks));

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
    static Set<Towers> getPossibleSwitchStates(Towers towers) {
        ImmutableSet.Builder<Towers> statesBuilder = ImmutableSet.builder();

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
}
