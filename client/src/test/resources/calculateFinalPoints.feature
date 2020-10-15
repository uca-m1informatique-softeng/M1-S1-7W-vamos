# As a player
# I want to sum up all my tokens
# So that a winner can be declared.
Feature: Calculate Final Points
  Background:
    Given the game has reached its last round
    And each player has finished his last turn
    #Or the player can buy the necessary resources from his neighbor
  Scenario: #Military conflicts
    Given the player has X Victory Tokens
    And the player has X Defeat Tokens
    When the player sums up his tokens
    Then his balance is X.

  Scenario: #**Treasury contents **
    Given the player’s balance is X
    When the player has X coins
    And X can be divided by 3
    Then the player’s balance will increase by X%3.

  Scenario:    #Wonder
    Given the player’s balance is X
    When the player has built X stages from the wonder’s board
    Then the player’s balance will increase by X victory points.

  Scenario:    #Civilian structures
    Given the player’s balance is X
    When the player has built structure X
    Then the player’s balance will increase by X victory points indicated on the structure’s card.

    #Scientific structures
  Scenario: #Sets of identical symbols
    Given the player’s balance is X
    When the player possesses the scientific structure 1 time
    Then the player’s balance will increase by 1 victory point.

    Given the player’s balance is X
    When the player possesses the scientific structure 2 times
    Then the player’s balance will increase by 4 victory points.

    Given the player’s balance is X
    When the player possesses the scientific structure 3 times
    Then the player’s balance will increase by 9 victory points.

    Given the player’s balance is X
    When the player possesses the scientific structure 4 times
    Then the player’s balance will increase by 16 victory points.

    Given the player’s balance is X
    When the player possesses the scientific structure 5 times
    Then the player’s balance will increase by 25 victory points.

    Given the player’s balance is X
    When the player possesses the scientific structure 6 times
    Then the player’s balance will increase by 36 victory points.

  Scenario:  #Sets of 3 different symbols
    Given the player’s balance is X
    When the player possesses 3 different scientific structures
    Then the player’s balance will increase by 7 victory points.

  Scenario:  #Commercial structures
    Given the player’s balance is X
    When the player possesses a commercial structure
    And the structure is worth X victory points
    Then the player’s balance will increase by X victory points.

  Scenario:  #Guilds
    Given the player’s balance is X
    When the player has a guild
    Then the player’s balance will increase by X victory points.
