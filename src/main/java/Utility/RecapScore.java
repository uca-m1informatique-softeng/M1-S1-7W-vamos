package Utility;
import Player.Player;

public class RecapScore
{
    int dataPoints;
    double moyenne;
    int nbVictory;
    boolean victory;
    public RecapScore(){}

    
    public RecapScore(Player player, boolean victory){
        this.victory = victory;
        dataPoints = player.computeScore();
    }

    public void addRecap(RecapScore recapScore)
    {
        if(recapScore.victory)
            nbVictory++;
       dataPoints += recapScore.getScore();

    }


    int getScore() {
        return dataPoints;
    }

    public void processAvgScore(int nombreParties)
    {
        moyenne = dataPoints/(double)nombreParties;
    }

    public double getAvgScore() {
        return moyenne;
    }
    public int getNbVictory(){
        return nbVictory;
    }

    @Override
    public String toString() {
        return " Player won :" + victory + " player points  " +  dataPoints + " totalPoints " + nbVictory;   }
    }

