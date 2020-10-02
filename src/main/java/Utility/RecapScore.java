package Utility;
import Player.Player;

public class RecapScore
{
    private int totalPoints;
    private int militaryPoints;
    private int sciencePoints;
    private int coins;
    double moyenne;
    int nbVictory;
    boolean victory;
    public RecapScore(){}


    public RecapScore(Player player, boolean victory){
        this.victory = victory;
        totalPoints = player.computeScore();
        militaryPoints = player.getMilitaryPoints();
        sciencePoints = player.getSciencePoint();
        coins = player.getCoins();
    }

    public void addRecap(RecapScore recapScore)
    {
        if(recapScore.victory)
            nbVictory++;
       totalPoints += recapScore.getScore();
       militaryPoints += recapScore.getMilitaryPoints();
       sciencePoints += recapScore.getSciencePoints();
        coins += recapScore.getCoins();

    }


    int getScore() {
        return totalPoints;
    }

    public void processAvgScore(int nombreParties)
    {
        moyenne = totalPoints/(double)nombreParties;
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

    public int getMilitaryPoints(){
        return militaryPoints;
    }
    public double getAvgScore() {
        return moyenne;
    }
    public int getNbVictory(){
        return nbVictory;
    }

    @Override
    public String toString() {
        return " Player won :" + victory + " player points  " +  totalPoints + " totalPoints " + nbVictory;   }
    }

