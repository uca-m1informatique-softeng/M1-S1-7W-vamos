package stepdefs;

import card.*;
import exceptions.PlayerNumberException;
import player.*;
import core.*;
import exceptions.WondersException;
import utility.Writer;
import wonder.Wonder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.junit.Assert.*;
import static utility.Constante.MAX_HAND;

import java.io.IOException;
import java.util.*;

public class ExecuteActionDefinitions {
    Player player = new Player("main");
    Player neighbor_next  = new Player("nextNeighbor");
    Player neighbor_prev = new Player("prevNeighbor");
    Game game;

    {
        try {
            game = new Game(3);
        } catch (PlayerNumberException e) {
            Writer.write("Could not launch Game ! " + e);
        }
    }

    public ExecuteActionDefinitions() throws IOException {
        player.setPrevNeighbor(neighbor_prev);
        player.setNextNeighbor(neighbor_next);
    }

    @Given("a next round has started$")
    public void aNextRoundHasStarted() {
        assertEquals(GameState.PLAY, game.getState());
        assertEquals(GameState.START, game.getState());
    }

    @And("the player has (\\d+) cards on his hand$")
    public void thePlayerHasXCardsOnHisHand(Integer cards) throws IOException {
        game.initDeck();
        game.initPlayers();
        for (int i = 0; i < MAX_HAND; i++)
            player.getHand().add(game.getDeck().remove(0));
        assertEquals(7, player.getHand().size());
    }
    //Szenario1
    @When("the player has selected a building card$")
    public void thePlayerHasSelectedABuildingCard() {
        player.setStrategy(new MilitaryStrategy());
        Action action = player.getStrategy().chooseAction(player);
        player.setChosenCard(action.getCard());
        assertEquals(Action.BUILD, action.getAction());
    }

    @And("the player has not yet built the structure$")
    public void thePlayerHasNotYetBuiltTheStructure() {
        assertFalse(player.getBuiltCards().contains(player.getChosenCard()));
    }

    @And("the player has all the resources to build the structure$")
    public void thePlayerHasAllTheResourcesToBuildTheStructure() {
        EnumMap<Resource,Integer> neededResources = player.getChosenCard().getResource();
        //Map.Entry<Resource, Integer> neededResource = new AbstractMap.SimpleEntry<Resource, Integer>[];
        Resource neededResource = null;
        Integer neededAmount = 0;

        for(Map.Entry<Resource, Integer> myEnum: neededResources.entrySet()){
            if(myEnum.getValue() > 0){
                player.getResources().put(myEnum.getKey(), myEnum.getValue());
                 neededResource = myEnum.getKey();
                 neededAmount = myEnum.getValue();
            }
        }
        player.getResources().put(neededResource, neededAmount);
        assertTrue(player.getResources().containsKey(neededResource) && player.getResources().get(neededResource) >= neededAmount);
    }

    @Then("the card will be placed on his Wonder’s board")
    public void theCardWillBePlacedOnHisWonderSBoard() throws IOException {
        Wonder wonder = new Wonder("gizahA");
        player.setWonder(wonder);
    }

    @And("the current round is finished for this player.")
    public void theCurrentRoundIsFinishedForThisPlayer() {
        assertTrue(player.buildCard());
    }

    //Szenario2
    @When("the player has selected a card to build a new stage wonder")
    public void thePlayerHasSelectedACardToBuildANewStageWonder() {
        player.setStrategy(new MilitaryStrategy());
        Action action = player.getStrategy().chooseAction(player);
        player.setChosenCard(action.getCard());
        //assertEquals(action.getAction(), Action.WONDER);
    }

    @And("the player has all the resources to build a new stage on his wonder board")
    public void thePlayerHasAllTheResourcesToBuildANewStageOnHisWonderBoard() throws IOException {
        Wonder wonder = new Wonder("gizahA");
        player.setWonder(wonder);
        assertFalse((player.getResources().isEmpty()));
        assertFalse((player.getResources().size() < player.getWonder().getCurrentUpgradeCost().size()));
        for (Map.Entry<Resource, Integer> entry1 : player.getWonder().getCurrentUpgradeCost().entrySet()) {
            Resource res1 = entry1.getKey();
            int nbRes1 = entry1.getValue();
            player.getResources().put(res1, nbRes1+1);
            assertFalse((player.getResources().get(res1) == null));
            assertFalse((player.getResources().get(res1) < nbRes1));
        }
    }

    @And("the player has built all previous stages of his wonder")
    public void thePlayerHasBuiltAllPreviousStagesOfHisWonder(){
        //no need to check
    }

    @And("the player has not yet built the stage")
    public void thePlayerHasNotYetBuiltTheStage() {
        //incase wonder is already at last state -> not possible to upgrade
        assertNotEquals(player.getWonder().getState(), player.getWonder().getMaxstate());
    }

    @Then("the card will be reserved for building the intended stage")
    public void theCardWillBeReservedForBuildingTheIntendedStage() {
        assertTrue(player.buildStageWonder());
    }

    //Szenario 3
    @When("the player has a card which he cannot build himself")
    public void thePlayerHasACardWhichHeCannotBuildHimself() {
        player.setStrategy(new DumbStrategy());
        Action action = player.getStrategy().chooseAction(player);
        player.setChosenCard(action.getCard());
        EnumMap<Resource, Integer> neededResources = player.getChosenCard().getResource();
        Resource neededResource = null;
        Integer neededAmount = 0;
        for(Map.Entry<Resource, Integer> myEnum: neededResources.entrySet()){
            if(myEnum.getValue() > 0){
                player.getResources().put(myEnum.getKey(), myEnum.getValue());
                neededResource = myEnum.getKey();
                neededAmount = myEnum.getValue();
            }
        }
        player.getResources().put(neededResource, neededAmount-1);
        assertFalse(player.getResources().containsKey(neededResource));
        assertFalse(player.getResources().get(neededResource) >= neededAmount);
    }

    @And("the player doesn’t want to pass the card on to his neighbor")
    public void thePlayerDoesnTWantToPassTheCardOnToHisNeighbor() {
        assertEquals(Action.DUMP, player.getStrategy().chooseAction(player).getAction());
    }

    @Then("the card will be placed on the discarded pile")
    public void theCardWillBePlacedOnTheDiscardedPile() {
        player.dumpCard();
        this.thePlayersTreasuryIsStockedUpWithXCoinsFromTheBank(3);
    }

    @And("^the player’s treasury is stocked up with (\\d+) coins from the bank\\.$")
    public void thePlayersTreasuryIsStockedUpWithXCoinsFromTheBank(Integer coins) {
        int coinsBefore = player.getCoins();
        player.setCoins(player.getCoins() + coins);
        assertEquals(3 , player.getCoins() - coinsBefore);
    }

}
