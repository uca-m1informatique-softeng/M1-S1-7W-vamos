# M1-S1-7W-vamos

## Notre équipe
- Ossama ASHRAF
- Sofian Moumen
- Sonja Deissenboeck
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
Selected method : algorithm Monte-Carlo, algorithm genetics\
Monte-Carlo and Genetics needs to manipulate AI with her parameters.\
To facilitate the training phase we add the functionality to pass a turn in the train phase.
#### Functionality
Choose the best action who is deducted through the best set of priority parameters : color, effect, best vp, ...\
For the card with the same priority, he refines the choice. Ex: Military +3 last turn and VP +8. He chooses the action who gives the choice that gives the best place in the ranking.

#### AI Parameters
##### Simple parameters
- Choice action(Build card, dumb card, Build wonder stage).
- Choice card color.
- Choice civilian card with the number of victory points given on parameter.
##### Advanced parameters
- The order of priority between action.
- The order of priority of the cards color(EnumList<CardColor>).
- The order of priority between cards of the same color.
- The order of priority of the card/wonder effect.
- The order of priority between a card with no effect and some card with effect.
- If some cards have the same priority, a method with no parameter choose the best action.
 
#### Parameters of Game
- Number of AIs.
- Type of AIs. (AI with moving strategy, AI DumbStrategy, AI Guaranteed…)
- The content of the card game.
- The order of the cards in the deck. (==> currently it’s random)
#### Data needed
- Classement with the details of the points.
