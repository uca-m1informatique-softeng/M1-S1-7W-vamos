package utility;

/**
 * This class will contains all const values (String,Integers..) needed to make the code clearer
 */
public final class Constante {
    //Server constants
    public static final String STATS = "Sending stats to the server" ;

    private Constante(){}


    // core game const values
    public final static int MAX_AGE = 3;
    public final static int MAX_ROUNDS =6;
    public final static int MAX_HAND = 7;
    public final static int MIN_PLAYER = 3;
    public final static int MAX_PLAYER = 3;


    public static final String STR_PLAYERS = "players";
    public static final String STR_CARDPOINTS = "cardPoints";
    public static final String STR_COST = "cost";
    public static final String STR_RESOURCE = "resource";
    public static final String STR_COLOREDCARDEFFECT = "coloredCardResourceEffect";
    public static final String STR_REWARD = "reward";



    public static final String STR_BATTLE_FORMAT = "%s fought %s and won %d military points!";




    /**
     *
     * Constantes pour les couleurs, le système est assez concis, il suffit d'appeler la constante avant l'écriture d'un string.
     * par exemple : Writer.ecrire( RED + " La phrase qui sera en couleur ")
     * En laissant la phrase comme telle, la couleur de base sera le rouge, donc toutes les sorties seront en rouge.
     *
     * Afin de colorer qu'une seule sortie texte en particulier, il faut rajouter la constante RESET à la fin comme ceci :
     * Writer.ecrire(RED + "la phrase qui sera en couleur "  + RESET + " Cette phrase sera en blanc ")
     *
     */
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String WHITE = "\033[0;30m";   // WHITE
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN

    // Bold
    public static final String WHITE_BOLD = "\033[1;30m";  // WHITE
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN

    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE


    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    //Mode de partie
    public final static String GAME_MODE ="game";
    public final static String STATS_MODE ="stats";
    public final static int NB_GAMES_STATS_MODE = 2000;
}

