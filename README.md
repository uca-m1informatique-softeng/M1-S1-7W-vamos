# M1-S1-7W-vamos

## Notre équipe
- Ossama ASHRAF
- Sofian Moumen
- Sonja Deissenboeck
- Kevin Levy
- Nicolas Zimmer

## Notre projet
Réalisation, en équipe ,d'un développement Java , du jeu Seven Wonders .

## Usage
Pour lancer le jeu : (edit configurations de la class Game)
- En mode jeu : program arguments : nombre de joueurs game 
Exemple : 3 game
- En mode statistique (2000 parties) : program arguments : nombre de joueurs stats  
Exemple : 3 stats
 
## Cahier des Charges TER
Implemented Rules
Game flow :
Game has 3 ages
Ages have 6 turns
Players choose a card in their hand, then pass their hand to their left neighbor
Military battles at the end of each age
Players can trade resources with their neighbors for coins
Card Decks are dealt during an age according to their age number (e.g. no age II card during age I)

Cards and Wonders :
Players can build cards
Players can dump cards
Players can build next stage of Wonder

Points and Resources :
Civil points
Scientific points (with their combinations)
Cards/Wonder stages cost resources
Cards/Wonders give resources
Cards give Military might

Special Effects :
Some cards and wonder stages have effects
Cards chaining (some cards allow the player to build cards for free)
Cards reduce the price of traded resources with neighbors (e.g. 1coin instead of 2 for Clay with your left neighbor)
