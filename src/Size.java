import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Creeare clasa Size.java, care va filtra fisierele descarcate
 * dupa marimea acestora. Pentru a utiliza aceasta clasa, trebuie
 * creeat un obiect folosind constructorul explicit, apoi apelate
 * functiile run si Printare.
 *
 * @author MateiMunteanu
 */

public class Size {
    /**
     * Initializeaza elementele primite din main.
     */
    private int maxim;
    private String root;
    private List<String> fisiere;

    /**
     *
     * @param maxim - nr maxim de MB pe care sa il aiba fisierul.
     * @param root_dir - fisierul root_dir din functia main (unde se descarca fisierele)
     */
    public Size(int maxim, String root_dir) {
        this.maxim = maxim;
        this.root = root_dir;
        this.fisiere = new ArrayList<String>();
    }

    /**
     * Rularea propriu-zisa a aplicatiei.
     */
    public void run()
    {
        try {
            File directory = new File(root);
            File[] fList = directory.listFiles();
            if (fList != null) {
                for (File fisier : fList) {
                    if (fisier.isFile()) {
                       if (fisier.length() < maxim)
                            fisiere.add(fisier.toString());
                    } else if (fisier.isDirectory()) {
                        this.root = fisier.toString();
                        run();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Nu am putut rula executia filtrarii.\n");

        }

    }

    /**
     * Printarea elementelor care au marimea mai mica de X MB
     */
    private void Printare()
    {
        for (String s : fisiere)
            System.out.println(s);
    }
}
