package utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestion d'écriture des fichiers sorties
 * On a 2 fichiers sorties qui enregistrent :
 * - le déroulement de la partie : DeroulementPartie.txt
 */
public final class Writer {
    static final String NOM_FICHIER = "DeroulementPartie.txt";
    private static FileWriter fileWriter;
    private static boolean mode;

    private Writer() {}

    private static void initGameFile()
    {
        try
        {
            fileWriter = new FileWriter(NOM_FICHIER, false);
        } catch (java.io.IOException e)
        {
            e.getStackTrace();
        }
    }

    public static void init(boolean mode)
    {
        Writer.mode = mode;
        if (mode)
        {
            initGameFile();
        }
    }

    public static void close()
    {
        try
        {
            if (mode)
            {
                fileWriter.close();
            }
        } catch (java.io.IOException e)
        {
            e.getStackTrace();
        }
    }
    public static void deleteFile() throws IOException {
        if (mode && !new File(NOM_FICHIER).delete()) throw new IOException("Could not delete File !");
    }

    public static void write(String str)
    {
        try
        {
            if (mode)
            {
                fileWriter.write(str + "\n");

            }
        } catch (java.io.IOException e)
        {
            e.getStackTrace();
        }
    }

    /**
     * Stops writing to the game file
     */
    public static void stopWriting() {
        mode = false;
    }

    /**
     * Resumes writing to the game file
     */
    public static void resumeWriting() {
        mode = true;
    }
}