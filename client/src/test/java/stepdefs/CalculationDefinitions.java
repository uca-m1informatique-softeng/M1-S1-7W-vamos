package stepdefs;
import card.Resource;
import player.Player;
import wonder.Wonder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

//TODO: methods that are used by multiple scenarios --> call from previous method with corresponding parameter
public class CalculationDefinitions{
    Resource resource;
    Player player = new Player("main");
    Player neighbor_next  = new Player("nextNeighbor");
    Player neighbor_prev = new Player("prevNeighbor");

    @Given("the game has reached its last round")
    public void theGameHasReachedItsLastRound() {

    }
    @And("each player has finished his last turn")
    public void eachPlayerHasFinishedHsLastTurn() {

    }
 //Military conflicts
    @Given("the player has X Victory Tokens")
    public void thePlayerHasXVictoryTokens() {
    }

    @And("the player has X Defeat Tokens")
    public void thePlayerHasXDefeatTokens() {
    }

    @When("the player sums up his tokens")
    public void thePlayerSumsUpHisTokens() {
    }

    @Then("his balance is X.")
    public void hisBalanceIsX() {
    }
//all scenarios
    @Given("the player’s balance is X")
    public void thePlayerSBalanceIsX() {
    }

    @When("the player has X coins")
    public void thePlayerHasXCoins() {
    }

    @And("X can be divided by {int}")
    public void xCanBeDividedBy(int arg0) {
    }

    @Then("the player’s balance will increase by X%{int}.")
    public void thePlayerSBalanceWillIncreaseByX(int arg0) {
    }
//Wonder
    @When("the player has built X stages from the wonder’s board")
    public void thePlayerHasBuiltXStagesFromTheWonderSBoard() {
    }

    @Then("the player’s balance will increase by X victory points.")
    public void thePlayerSBalanceWillIncreaseByXVictoryPoints() {
    }
//Civilian structures
    @When("the player has built structure X")
    public void thePlayerHasBuiltStructureX() {
    }

    @Then("the player’s balance will increase by X victory points indicated on the structure’s card.")
    public void thePlayerSBalanceWillIncreaseByXVictoryPointsIndicatedOnTheStructureSCard() {
    }
//#Scientific structures
    //Sets of identical symbols
    @When("the player possesses the scientific structure {int} time")
    public void thePlayerPossessesTheScientificStructureTime(int arg0) {
    }

    @Then("the player’s balance will increase by {int} victory point.")
    public void thePlayerSBalanceWillIncreaseByVictoryPoint(int arg0) {
    }

    @When("the player possesses the scientific structure {int} times")
    public void thePlayerPossessesTheScientificStructureTimes(int arg0) {
    }

    @Then("the player’s balance will increase by {int} victory points.")
    public void thePlayerSBalanceWillIncreaseByVictoryPoints(int arg0) {
    }
//Sets of 3 different symbols
    @When("the player possesses {int} different scientific structures")
    public void thePlayerPossessesDifferentScientificStructures(int arg0) {
    }
//Commercial structures
    @When("the player possesses a commercial structure")
    public void thePlayerPossessesACommercialStructure() {
    }

    @And("the structure is worth X victory points")
    public void theStructureIsWorthXVictoryPoints() {
    }

//Guilds
    @When("the player has a guild")
    public void thePlayerHasAGuild() {
    }
}