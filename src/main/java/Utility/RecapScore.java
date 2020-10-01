package Utility;


import Player.Player;

public class RecapScore
{
    int[] data= new int[2];
    int dataPoints;
    double moyenne;
    int nbVictory;
    boolean victoire;
    public RecapScore(){}

    public RecapScore(Player player, boolean victoire){
        this.victoire = victoire;
        dataPoints = player.computeScore();
    }

    public void addRecap(RecapScore recapScore)
    {
        if(recapScore.victoire)
            nbVictory++;
       dataPoints += recapScore.getScore();

    }

    int victoire(){
        return data[0];
    }
    int getScore(){
        return dataPoints;
    }
    int getBatiments(){
        return data[2];
    }
    int getCivilisation(){
        return data[3];
    }
    int getOuvriers(){
        return data[4];
    }
    int getAgriculture(){
        return data[5];
    }

    public void calculerMoyenne(int nombreParties)
    {
        moyenne = dataPoints/(double)nombreParties;
    }

    public double victoireMoyenne(){
        return moyenne;
    }
    public double getScoreMoyenne() {
        return moyenne;
    }
    public int getNbVictory(){
        return nbVictory;
    }

    @Override
    public String toString() {
        return "Victoire du joueur :" + victoire + " nombre de points du joueur " +  dataPoints + " nombre de victoires " + nbVictory;   }

    /*public double getBatimentsMoyenne(){
        return moyenne[2];
    }
    public double getCivilisationMoyenne(){
        return moyenne[3];
    }
    public double getOuvriersMoyenne(){
        return moyenne[4];
    }
    public double getAgricultureMoyenne(){
        return moyenne[5];
    }
    public double getPointsCivilisationMoyenne()
    {
        return moyenne[6];
    }
    public double getPointsAgricultureMoyenne()
    {
        return moyenne[7];
    }
    public double getPointsOutilsMoyenne()
    {
        return moyenne[8];
    }
    public double getPointsOuvriersMoyenne()
    {
        return moyenne[9];
    }
    public double getPointsBatimentsMoyenne()
    {
        return moyenne[10];
    }

    public int getPointsCivilisation(){
        return dataPoints[0];
    }
    public int getPointsAgriculture(){
        return dataPoints[1];
    }
    public int getPointsOutils(){
        return dataPoints[2];
    }
    public int getPointsOuvriers(){
        return dataPoints[3];
    }
    public int getPointsBatiments(){
        return dataPoints[4];
    }*/

    }

