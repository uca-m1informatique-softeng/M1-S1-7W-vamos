#As a player
#I want to trade with my neighbors
#So that I receive resources.
Feature: Commerce Feature
  Background:
    Given a player wants to have the resource {Resource} from his neighbor
    And the neighboring city produces the resource {Resource}
    And the player has 2 coins for trade

  Scenario: Executing a trade action with neighbors
    When the player requests the resource {Resource}
    Then  the player should hand out 2 of his coins
    #And his neighbor should trade it for the resource {Resource}
