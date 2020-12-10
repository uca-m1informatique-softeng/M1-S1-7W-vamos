# M1-S1-7W-vamos

## Notre équipe
- Ossama ASHRAF
- Kevin Levy
- Nicolas Zimmer
 
# Cahier des Charges TER
## Implemented Rules
### Game flow :
 - Game has 3 ages
 - Ages have 6 turns
 - Players choose a card in their hand, then pass their hand to their left neighbor
 - Military battles at the end of each age
 - Players can trade resources with their neighbors for coins
 - Card Decks are dealt during an age according to their age number (e.g. no age II card during age I)
 - At the end of Age III, points are counted and the winner is printed

### Cards and Wonders :
 - Players can build cards
 - Players can dump cards
 - Players can build next stage of Wonder

### Points and Resources :
- Civil points
- Scientific points (with their combinations)
- Cards/Wonder stages cost resources
- Cards/Wonders give resources
- Cards give Military might

### Special Effects :
- All card and wonder stage effects were implemented .

## AI Levels
### Guaranteed AI
- If player gets 1 specific science point: whenever he again has the choice between science points, he should go for the same science point as before .
- If a player has the chance to build marketplace card he should do it .
- Focus on military cards especially in age 3 and towards the ends of each age .
- Be aware of military cards of neighbors and weigh out whether it would be important to win a battle or not.
- Analyze neighbor's strategies & try to block them:
  - Build cards that would give them a disadvantage or dump it in order to get coins .     
  - Focus on same colored cards
- Prioritize color of cards: depending on age
  - Age 1: green -> grey
  - Age 2: green -> brown -> grey
  - Age 3: green -> red

### Ambitious AI 
For this AI, we will use the Monte-Carlo algorithm.\
The algorithm will need to be quick enough to simulate a real player. It shouldn't take more than 20-30 seconds to make a decision.\
In order to do this, we will need to limit the depth of the algorithm's recursivity, thus we will need to use a heuristic.

#### Heuristic Features
The heuristic will need to take into account the following :
- The players' rankings
- The ability to block the other players
- The potential for science points combinations (depending on the cards currently played)
- The free cards the player can get with his currently built cards
- The players' military might, depending on the current age, and turn
More features could be added in the future, if possible.

#### IA Evaluation
In order to choose the best recursivity depth, number of Monte-Carlo simulations, and the best heuristics, we will need to make statistics on these parameters.\
The best set of parameters will then be chosen, tested against our Guaranteed AI, and finally on other AIs if possible.

-----------------------------------------------------------------------------------------------
## Implemented
### Guaranteed AI
- If player gets 1 specific science point: whenever he again has the choice between science points, he should go for the same science point as before .
- If a player has the chance to build marketplace card he should do it .
- The player builds military cards if he has less military points than his neighbors and of course if he can afford them .
- Analyze neighbor's strategies & try to block them:
  - Build cards that would give them a disadvantage or dump it in order to get coins .
  - Focus on same colored cards
- Prioritize color of cards: depending on age
  - Age 1: green -> grey
  - Age 2: green -> brown -> grey
  - Age 3: green -> red
## Modifications
### Guaranteed AI
- Focus on military cards especially in age 3 and towards the ends of each age .
- Be aware of military cards of neighbors and weigh out whether it would be important to win a battle or not.
- We remarked that the above two features were not as good as we thought, so we decided to combine them into 
one feature : the player builds military cards if he has less military points than his neighbors and of course if he can afford them .
  
- We modified the following features :
  - Prioritize color of cards: depending on age
    - Age 1: green -> grey
    - Age 2: green -> brown -> grey
    - Age 3: green -> red
  - Into :
    - Prioritize color of cards: depending on age
      - Age 1: green -> grey -> blue
      - Age 2: green -> brown -> grey -> blue
      - Age 3: green -> blue