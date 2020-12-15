
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.EOFException;

/**
 * /*
 * Creare clasa de filtrare dupa tip, astfel incat utilizatorul
 * sa poata vedea anumite fisiere avand extensii comune ( de ex : ".txt"
 * pentru fisiere text, ".java" pentru fisiere java s.a.m.d.)
 *
 * @author MateiMunteanu
 */

public class FilterByType {
    /**
     * initializam elementele primite din main
     */
    private String extensie;
    private String root;
    private List<String> fisiere;

    /**
     * @param url      - va fi fisierul de root unde se salveaza paginile
     * @param extensie - extensia pe care o cautam
     */
    public FilterByType(String url, String extensie) {
        this.root = url;
        this.extensie = extensie;
        this.fisiere = new ArrayList<String>();
    }

    /**
     * Rularea propriu-zisa a programului.
     * fListul va contine toate elementele din fisierul root.
     * Daca este de tip file si are extensia care ne intereseaza
     * punem adresa relativa a acesteia intr-o lista de stringuri.
     * Daca nu este de tip file, merge recursiv in acesta.
     */
    public void run() {
        try {
            File directory = new File(root);

            File[] fList = directory.listFiles();
            if (fList != null) {
                for (File fisier : fList) {
                    if (fisier.isFile()) {
                        Path path = Paths.get(fisier.toString());
                        String name = path.getFileName().toString();
                        int index = name.lastIndexOf('.');
                        if (index > 0) {
                            String ext = name.substring(index + 1);
                            if (ext.equals(extensie))
                                fisiere.add(name);
                        }
                    } else if (fisier.isDirectory()) {
                        this.root = fisier.toString();
                        run();
                    }
                }
            }
        } catch (Exception e) {
            Log.getInstance().writeToFile("ERR : Nu am putut rula executia filtrarii dupa extensia " + this.extensie + ".");
            System.out.println("Nu am putut rula filtrarea dupa tip.");

        }
    }

    /**
     * Afisare de elemente care au extensia ce ne intereseaza.
     */
    public void Printare() {
        if (this.fisiere.isEmpty()) {
            System.out.println("Nu exista vreun fisier avand extensia " + this.extensie + ".");
            Log.getInstance().writeToFile("WAR : Nu exista vreun fisier avand extensia " + this.extensie + ".");
        } else {

            for (String s : fisiere)
                System.out.println("Fisier : " + s);


            Log.getInstance().writeToFile("INFO : Am realizat filtrarea fisierelor avand extensia : " +this
            .extensie + ".");
        }
    }
}

