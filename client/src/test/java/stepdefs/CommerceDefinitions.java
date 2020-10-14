package stepdefs;

import card.Resource;
import player.Player;
import wonder.Wonder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.io.IOException;
import java.util.EnumMap;

import static org.junit.Assert.*;

/**
 * currently the success of the tests is a little bit arbitrary because it depends on the
 * strategy a player will chose and also the cards he has on his hands
 */
public class CommerceDefinitions {
    Resource resource;
    Player player = new Player("main");
    Player neighbor_next  = new Player("nextNeighbor");
    Player neighbor_prev = new Player("prevNeighbor");

    @Given("a player wants to have the resource \\\\{Resource} from his neighbor")
    public void aPlayerWantsToHaveTheResourceResourceFromHisNeighbor() {
        resource = Resource.STONE;
    }
    @And("the neighboring city produces the resource \\\\{Resource}")
    public void theNeighboringCityProducesTheResourceResource() throws IOException {
        Wonder wonder = new Wonder("gizahA");
        player.setNextNeighbor(neighbor_next);
        player.setPrevNeighbor(neighbor_prev);
        neighbor_next.setWonder(wonder);
        assertEquals(neighbor_next.getWonder().getProducedResource(), resource);
    }

    @And("the player has 2 coins for trade")
    public void has_two_coins(){
        player.setCoins(5);
        assertTrue(player.getCoins() >= 2);
    }

    @When("the player requests the resource \\\\{Resource}")
    public void request_resource() {
        neighbor_next.getResources().put(resource, 4);
        Boolean bought = player.buyResource(resource, 1,neighbor_next);
        assertTrue(bought);
        //player doesn't have enough coins
        player.setCoins(1);
        bought = player.buyResource(resource, 1,neighbor_next);
        assertFalse(bought);
    }

    @Then("the player should hand out 2 of his coins")
    public void handout_coins() {
        Integer playerCoinsBefore = player.getCoins();
        player.setCoins(player.getCoins() - 2);
        Integer neighborCoinsBefore = neighbor_next.getCoins();
        neighbor_next.setCoins(neighbor_next.getCoins() + 2);
        assertTrue(playerCoinsBefore > player.getCoins());
        assertTrue(neighborCoinsBefore < neighbor_next.getCoins());
    }

    @And("his neighbor should trade it for the resource \\\\{Resource}")
    public void hisNeighborShouldTradeItForTheResourceResource() {
        EnumMap<Resource, Integer> boughtResources = player.getBoughtResources();
        assertNotNull(boughtResources);
    }


}
