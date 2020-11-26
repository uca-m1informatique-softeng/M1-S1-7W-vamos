package player;

import java.security.SecureRandom;

/**
 * Abstract class used to define the Strategy of a player.
 * Children from strategy should implement method chooseAction, that returns an action based on the state of a player.
 */
public abstract class Strategy {

    protected SecureRandom rand = new SecureRandom();

    /**
     * Method used to choose an action based on the player's status (its hand, its resources, its neighbors, etc...)
     * @param player The player whose action should be chosen.
     * @return The Action the player should execute.
     */
    public abstract Action chooseAction(Player player);

    public abstract String toString();
}
