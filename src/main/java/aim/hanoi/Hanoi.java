package aim.hanoi;

import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Log4j2
public class Hanoi {

    private Set<Towers> visitedStates = new HashSet<>();

    private void iterativeBfs(int numberOfDisks) {
        Queue<Towers> queue = new LinkedList<>();
        Towers rootNode = Towers.initState(numberOfDisks);
        Towers end = Towers.endState(numberOfDisks);

        queue.add(rootNode);
        visitedStates.add(rootNode);
        log.error(rootNode);

        while (!queue.isEmpty()) {
            Towers towers = queue.remove();
            Set<Towers> unvisitedChildNodes = towers.getPossibleSwitchStates().stream()
                    .filter(adjacent -> !visitedStates.contains(adjacent))
                    .collect(Collectors.toSet());
            if (!unvisitedChildNodes.isEmpty()) {
                unvisitedChildNodes.forEach(child -> {
                    visitedStates.add(child);
                    log.error(child);
                    queue.add(child);
                });
            }
            if (unvisitedChildNodes.contains(end)) {
                break;
            }
        }
    }

    private void iterativeDfs(int numberOfDisks) {
        Stack<Towers> towersStack = new Stack<>();
        Towers rootNode = Towers.initState(numberOfDisks);
        Towers end = Towers.endState(numberOfDisks);

        towersStack.push(rootNode);
        visitedStates.add(rootNode);
        log.error(rootNode);

        while (!towersStack.empty()) {
            Towers towers = towersStack.peek();
            Set<Towers> unvisitedChildNodes = towers.getPossibleSwitchStates().stream()
                    .filter(adjacent -> !visitedStates.contains(adjacent))
                    .collect(Collectors.toSet());
            if (unvisitedChildNodes.isEmpty()) {
                towersStack.pop();
            } else {
                unvisitedChildNodes.forEach(child -> {
                    visitedStates.add(child);
                    log.error(child);
                    towersStack.push(child);
                });
            }
            if (unvisitedChildNodes.contains(end)) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        Hanoi hanoi = new Hanoi();
        hanoi.iterativeDfs(3);
        hanoi.iterativeBfs(3);
    }
}
