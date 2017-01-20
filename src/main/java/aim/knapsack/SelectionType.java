package aim.knapsack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Piotr
 */
@Getter
@RequiredArgsConstructor
enum SelectionType {
    SimpleTournament, Roulette;
}
