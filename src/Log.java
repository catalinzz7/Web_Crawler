import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalTime;
import java.util.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Creeare clasa Log
 * Vom scrie in fisier fiecare Warning, Eroare etc, pentru o buna
 * gestionare a aplicatiei. Se va nota data si ora cand acestea au fost
 * accesate.
 *
 * se va rula in main : Log.getInstance().writeToFil("Warning : Fisierul nu a fost gasit");
 * Clasa va fi de tipul Singleton
 * @author MateiMunteanu
 *
 */
public class Log {
    private static PrintWriter out;
    private static final Log inst = new Log();

    /**
     * Constructoul Privat, care va apela functia super
     * Dupa aceea vom creea fisierul de log.
     */
    private Log()
    {
        super();
    }

    /**
     * Ne vom lua un obiect pe care il vom formata pentru  a avea patternul  dd-MM-yyyy urmat de timp in ore, minute si secunde.
     * @param str - stringul ce va fi scris in fisier
     *
     */
    public synchronized void writeToFile (String str){
        try {
            FileWriter file = new FileWriter("src\\data\\log.txt", true);
            out = new PrintWriter(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        LocalDateTime myLocalTime = LocalDateTime.now();
        DateTimeFormatter myFormateLocalTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String dateTime = myLocalTime.format(myFormateLocalTime);
        out.println("[" + dateTime + "]  :  " +  str);
        out.close();
    }

    /**
     *
     * @return obiectul unic de tip singleton
     */
    public static Log getInstance(){
        return inst;
    }
}