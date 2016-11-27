package aim.hanoi;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Log4j2
public class Hanoi {

    private static List<Towers> iterativeBfs(int numberOfDisks) {
        Queue<Towers> queue = new LinkedList<>();
        Towers rootNode = Towers.initState(numberOfDisks);
        Towers end = Towers.endState(numberOfDisks);

        queue.add(rootNode);
        Set<Towers> visitedStates = Sets.newHashSet(rootNode);
        List<Towers> totalPath = Lists.newArrayList(rootNode);

        while (!queue.isEmpty()) {
            Towers towers = queue.remove();
            Set<Towers> unvisitedChildNodes = towers.getPossibleSwitchStates().stream()
                    .filter(adjacent -> !visitedStates.contains(adjacent))
                    .collect(Collectors.toSet());
            if (!unvisitedChildNodes.isEmpty()) {
                for (Towers child : unvisitedChildNodes) {
                    visitedStates.add(child);
                    totalPath.add(child);
                    queue.add(child);
                }
            }
            if (unvisitedChildNodes.contains(end)) {
                break;
            }
        }
        return totalPath;
    }

    private static List<Towers> iterativeDfs(int numberOfDisks) {
        Stack<Towers> towersStack = new Stack<>();
        Towers rootNode = Towers.initState(numberOfDisks);
        Towers end = Towers.endState(numberOfDisks);

        towersStack.push(rootNode);
        Set<Towers> visitedStates = Sets.newHashSet(rootNode);
        List<Towers> totalPath = Lists.newArrayList(rootNode);

        while (!towersStack.empty()) {
            Towers towers = towersStack.peek();
            Set<Towers> unvisitedChildNodes = towers.getPossibleSwitchStates().stream()
                    .filter(adjacent -> !visitedStates.contains(adjacent))
                    .collect(Collectors.toSet());
            if (unvisitedChildNodes.isEmpty()) {
                towersStack.pop();
            } else {
                for (Towers child : unvisitedChildNodes) {
                    visitedStates.add(child);
                    totalPath.add(child);
                    towersStack.push(child);
                }
            }
            if (unvisitedChildNodes.contains(end)) {
                break;
            }
        }
        return totalPath;
    }

    private static List<Towers> aStar(int numberOfDisks) {
        Towers init = Towers.initState(numberOfDisks);
        Towers end = Towers.endState(numberOfDisks);

        Set<Towers> closedSet = Sets.newHashSet();
        Set<Towers> openSet = Sets.newHashSet(init);
        Map<Towers, Towers> cameFrom = Maps.newHashMap();

        // For each node, the cost of getting from the start node to that node.
        Map<Towers, Integer> gScore = Maps.newHashMap();
        gScore.put(init, init.getGScore());

        // For each node, the total cost of getting from the start node to the goal by passing by that node.
        // That value is partly known, partly heuristic.
        Map<Towers, Integer> fScore = Maps.newHashMap();
        fScore.put(init, init.getGScore());

        while (CollectionUtils.isNotEmpty(openSet)) {
            @SuppressWarnings("OptionalGetWithoutIsPresent") Towers current = openSet.stream().min(Towers.F_SCORE_COMPARATOR).get();
            if (current.equals(end)) {
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Towers neightbor : current.getPossibleSwitchStates()) {
                if (closedSet.contains(neightbor)) {
                    continue;
                }
                int tentactiveGScore = current.getGScore() + 1;
                if (!openSet.contains(neightbor)) {
                    openSet.add(neightbor);
                } else if (tentactiveGScore >= neightbor.getGScore()) {
                    continue;
                }
                cameFrom.put(neightbor, current);
                gScore.put(neightbor, tentactiveGScore);
                fScore.put(neightbor, neightbor.getFScore());
            }
        }
        return Collections.emptyList(); // unreachable
    }

    private static List<Towers> reconstructPath(final Map<Towers, Towers> cameFrom, final Towers endState) {
        Towers current = endState;
        final List<Towers> totalPath = Lists.newArrayList(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        return Lists.reverse(totalPath);
    }

    @SuppressWarnings("SimplifyStreamApiCallChains")
    public static void main(String[] args) {
        int numberOfDisks = 3;
        List<Towers> dfsPath = iterativeDfs(numberOfDisks);
        List<Towers> bfsPath = iterativeBfs(numberOfDisks);
        List<Towers> aStarPath = aStar(numberOfDisks);
        log.error("\n\n\nBFS solution ({} steps):", bfsPath.size());
        bfsPath.stream().forEachOrdered(log::error);
        log.error("\n\n\nDFS solution ({} steps):", dfsPath.size());
        dfsPath.stream().forEachOrdered(log::error);
        log.error("\n\n\nA* solution ({} steps):", aStarPath.size());
        aStarPath.stream().forEachOrdered(log::error);
    }
}
