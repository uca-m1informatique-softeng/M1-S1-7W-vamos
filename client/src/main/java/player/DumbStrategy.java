package player;

/**
 * This class extends Strategy.
 * DumbStrategy is a random strategy implementation, that should represent a "dumb" player.
 */
public class DumbStrategy extends Strategy {
    /**
     * Implementation of Strategy::chooseAction(player player).
     * This implementation chooses a random card and a random action available from its hand.
     * @param player The player whose action should be chosen.
     * @return A random action.
     */
    @Override
    public Action chooseAction(Player player) {
        int index = rand.nextInt(player.hand.size());
        int action = rand.nextInt(3) + 1;

        return new Action(player.getHand().get(index), action);
    }

    @Override
    public String toString() {
        return "Dumb";
    }
}
