# M1-S1-7W-vamos

## Notre équipe
- Ossama ASHRAF
- Sofian Moumen
- Sonja Deissenboeck
- Kevin Levy
- Nicolas Zimmer

## Notre projet
Réalisation en équipe du jeu 7 Wonders, en Java.

## Usage
Pour lancer le jeu : (edit configurations de la class Game)
- En mode jeu : program arguments : nombre de joueurs game 
Exemple : 3 game
- En mode statistique (2000 parties) : program arguments : nombre de joueurs stats  
Exemple : 3 stats
 
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
##### Card effects :
- Some cards and wonder stages have effects
- Cards chaining (some cards allow the player to build cards for free)
- Cards reduce the price of traded resources with neighbors (e.g. 1coin instead of 2 for Clay with your left neighbor)
- On certain cards, player has the choice between one resource or the other : ResourceChoiceEffect
- Haven card gives 1 coin per brown card present in a player’s city when it is played. At the end of the game, the card is worth 1 victory point for each brown card present in the player’s city.
- Lighthouse card gives 1 coin per yellow card in the player’s city, itself included, when it is played. At the end of the game, the card is worth 1 victory point for each yellow card present in a player’s city.
- Vineyard card is worth 1 coin per brown card built in the player’s city AND in the two neighboring cities
- Bazar card is worth 2 coins per gray card built in the player’s city AND in the two neighboring cities.
- Chamber of commerce card gives 2 coins for each gray card present in the player’s city when it comes into play. At the end of the game, the card is worth 2 victory points for each gray card present in the player’s city.
- Spies,Magistrates,Workers,Traders,Philosophers guilds give 1 victory point for each red,blue,brown,yellow,green (respectively)card present in both neighboring cities.
- Craftmans Guild gives 2 victory points for each gray card present in the neighboring cities.
- Shipowners Guild gives 1 victory point for each brown, gray and purple card in your city.
- Strategists Guild: 1 victory point for each defeat token present in the neighboring cities.
- Scientists Guild: the player gains an extra scientific symbol of his or her choice
- Builders Guild: 1 victory point for each Wonder stage built in the neighboring cities AND in your own city.
##### Wonder stages effects :
- Once per age, a player can construct a building from his or her hand for free.
- The player can play the last card of each age instead of discarding it. This card can be played by paying its cost, discarded to gain 3 coins or used in the construction of his or her Wonder
- The player can look at all cards discarded since the beginning of the game, pick one and build it for free.
- The player can, at the end of the game, “copy” a Guild of his or her choice (purple card), built by one of his or her two neighboring cities.
