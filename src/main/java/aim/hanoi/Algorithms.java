package aim.hanoi;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

@SuppressWarnings("WeakerAccess")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Algorithms {

    static List<Towers> iterativeBfs(int numberOfDisks) {
        Queue<Towers> queue = new LinkedList<>();
        Towers rootNode = Towers.initState(numberOfDisks);
        Towers end = Towers.endState(numberOfDisks);

        queue.add(rootNode);
        Set<Towers> visitedStates = Sets.newHashSet(rootNode);
        List<Towers> totalPath = Lists.newArrayList(rootNode);

        while (!queue.isEmpty()) {
            Set<Towers> unvisitedChildNodes = getUnvisitedChildNodes(queue.remove(), visitedStates);
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
        return getFinalPath(end, totalPath);
    }

    static List<Towers> iterativeDfs(int numberOfDisks) {
        Stack<Towers> towersStack = new Stack<>();
        Towers rootNode = Towers.initState(numberOfDisks);
        Towers end = Towers.endState(numberOfDisks);

        towersStack.push(rootNode);
        Set<Towers> visitedStates = Sets.newHashSet(rootNode);
        List<Towers> totalPath = Lists.newArrayList(rootNode);

        while (!towersStack.empty()) {
            Set<Towers> unvisitedChildNodes = getUnvisitedChildNodes(towersStack.peek(), visitedStates);
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
        return getFinalPath(end, totalPath);
    }

    private static Set<Towers> getUnvisitedChildNodes(Towers state, final Set<Towers> visited) {
        Set<Towers> unvisited = new HashSet<>();
        for (Towers adjacent : state.getPossibleSwitchStates()) {
            if (!visited.contains(adjacent)) {
                unvisited.add(adjacent);
            }
        }
        return unvisited;
    }

    private static List<Towers> getFinalPath(final Towers end, final List<Towers> totalPath) {
        List<Towers> finalPath = new ArrayList<>();
        for (Towers state : totalPath) {
            finalPath.add(state);
            if (state.equals(end)) {
                break;
            }
        }
        return finalPath;
    }

    static List<Towers> aStar(int numberOfDisks) {
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
            @SuppressWarnings("OptionalGetWithoutIsPresent")
            Towers current = openSet.stream().min(Towers.F_SCORE_COMPARATOR).get();
            if (current.equals(end)) {
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Towers neighbor : current.getPossibleSwitchStates()) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                int tentativeGScore = current.getGScore() + 1;
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= neighbor.getGScore()) {
                    continue;
                }
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, neighbor.getFScore());
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
}
