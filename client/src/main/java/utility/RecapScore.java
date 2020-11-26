package utility;
import player.*;

/**
 *  Recap score class, which will hold every data of a player game.
 *  This class will be used for the stats mode, in order to aggregate data of several thousands of games and display it to the user.
 */
public class RecapScore {
    /**
     * every data we need to store stats and then send it to the server
     */
    private int totalPoints;
    private int militaryPoints;
    private int sciencePoints;
    private int coins;
    private int civilianPoints;
    public String strategy;

    double moyenne;
    int nbWin;
    boolean win;

    public RecapScore() {
    }

    public RecapScore(Player player, boolean victory) {
        this.win = victory;
        totalPoints = player.computeScore();
        militaryPoints = player.getFightPoints();
        sciencePoints = player.getSciencePoint();
        coins = player.getCoins();
        civilianPoints = player.getVictoryPoint();
        this.strategy = player.getStrategy().toString();
    }

    public RecapScore(String victory, String totalPoint, String militaryPoints, String sciencePoints, String coins) {
        this.nbWin = Integer.parseInt(victory);
        this.totalPoints = Integer.parseInt(totalPoint);
        this.militaryPoints = Integer.parseInt(militaryPoints);
        this.sciencePoints = Integer.parseInt(sciencePoints);
        this.coins = Integer.parseInt(coins);
    }

    /**
     * This method will add a recapScore object to the instantiated object.
     * Thi instantiated object will then hold the data of every object which were added to it
     *
     * @param recapScore
     */
    public void addRecap(RecapScore recapScore) {
        if (recapScore.win)
            nbWin++;
        totalPoints += recapScore.getScore();
        militaryPoints += recapScore.getMilitaryPoints();
        sciencePoints += recapScore.getSciencePoints();
        coins += recapScore.getCoins();
        civilianPoints += recapScore.civilianPoints;
        this.strategy = recapScore.getStrategy();
    }


    int getScore() {
        return totalPoints;
    }

    public void processAvgScore(int nombreParties) {
        moyenne = totalPoints / (double) nombreParties;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getSciencePoints() {
        return sciencePoints;
    }

    public int getCoins() {
        return coins;
    }

    public int getMilitaryPoints() {
        return militaryPoints;
    }

    public double getAvgScore() {
        return moyenne;
    }

    public int getNbVictory() {
        return nbWin;
    }

    @Override
    public String toString() {
        return strategy + nbWin + militaryPoints + sciencePoints + totalPoints + coins;
    }


    public String getStrategy() {
        return this.strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public int getCivilianPoints() { return civilianPoints; }
}