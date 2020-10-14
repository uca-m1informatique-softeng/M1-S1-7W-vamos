#As a player I want to choose a card from my hand
# #So that I can execute actions in the current round.
Feature: Execute Action Feature
  Background:
    Given a next round has started
    And the player has 7 cards on his hand
    #Or the player can buy the necessary resources from his neighbor
  Scenario: #1. Action: Build structure
    When the player has selected a building card
    And the player has not yet built the structure
    And the player has all the resources to build the structure
    Then the card will be placed on his Wonder’s board
    And the current round is finished for this player.
  Scenario: #2. Action: Build a stage of their wonder
    When the player has selected a card to build a new stage wonder
    And the player has all the resources to build a new stage on his wonder board
    And the player has built all previous stages of his wonder
    And the player has not yet built the stage
    #Or the player can buy the necessary resources from his neighbor
    Then the card will be reserved for building the intended stage
    And the current round is finished for this player.
  Scenario: #3. Action: Discard the card to gain 3 coins
    When the player has a card which he cannot build himself
    And the player doesn’t want to pass the card on to his neighbor
    Then the card will be placed on the discarded pile
    And the player’s treasury is stocked up with 3 coins from the bank.
    And the current round is finished for this player.
