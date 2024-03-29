package effects;

import card.Card;
import card.CardColor;
import card.Resource;
import player.Player;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * This class is just used as an abstraction level to define what special effects a card can have.
 * Those effects range from choosing between two resources on a card, to having to pay only one coin for trading
 * resources with your neighbors.
 */
public abstract class Effect {
    public abstract void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards);
}
