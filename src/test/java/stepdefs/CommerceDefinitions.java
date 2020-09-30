package stepdefs;

import Card.Resource;
import Player.Player;
import Core.Wonder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.io.IOException;

import static org.junit.Assert.*;

public class CommerceDefinitions {
    Resource resource;
    Player player = new Player("main");
    Player neighbor  = new Player("neighbor");

    @Given("a player wants to have the resource \\{Resource} from his neighbor")
    public void player_needs_resource() {
        resource = Resource.STONE;
    }
    @And("the neighboring city produces the resource \\{Resource}")
    public void theNeighboringCityProducesTheResource() throws IOException {
        Wonder wonder = new Wonder("GIZAH");
        neighbor.setWonder(wonder);
        assertEquals(neighbor.getWonder().getProductedResource(), resource);
    }

    @And("the player has 2 coins for trade")
    public void has_two_coins(){
        player.setCoins(5);
        assertTrue(player.getCoins() >= 2);
    }

    @When("the player requests the resource \\{Resource}")
    public void request_resource() {
        player.buyResource(resource, neighbor);
    }

    @Then("the player should hand out 2 of his coins")
    public void handout_coins() {
        player.setCoins(player.getCoins() - 2);
        neighbor.setCoins(neighbor.getCoins() + 2);
    } 

}
