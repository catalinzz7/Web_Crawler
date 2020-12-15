import java.io.*;
import java.util.*;

/**
 * Clasa Search implementeaza cautarea unor URL-uri din directorul radacina dupa un anumit cuvant cheie.
 * Cauta recursiv in directorul radacina si afiseaza URL-urile dupa frecventa de aparitie a cuvantului cheie.
 *
 * @author Dan Alexandru
 */

public class Search {
    /**
     * mDir reprezinta root_dir-ul
     * urls contine URL-urile in care apare cuvantul cheie si numarul de aparitii
     * mKey reprezinta cuvantul cheie
     */

    File mDir;
    Map<Integer, String> urls = new HashMap<Integer, String>();
    String mKey;

    /**
     * constructorul
     */
    public Search() {
    }

    /**
     * functia de verificare a existentei root_dir
     */
    public void search(String key, String dir) {
        this.mDir = new File(dir);
        this.mKey = key;
        if (mDir.exists()) {
            searchInTextFiles(mKey, mDir);
            Log.getInstance().writeToFile(" INFO: Caut cuvantul " + mKey + " in " + mDir + "!\n");
            print();
        } else {
            System.out.println("Nu exista fisierul!");
            Log.getInstance().writeToFile("Warning : Fisierul " + mDir + " nu a fost gasit" + "!\n");
        }

    }

    /**
     * Sortare URL-uri descrescator dupa frecventa de aparitie a cuvantului cheie si afisarea acestora
     */
    public void print() {
        TreeMap<Integer, String> sorted = new TreeMap<>(Collections.reverseOrder());
        sorted.putAll(urls);
        int i = 1;
        System.out.println("\nCautarea  cheii " + mKey + ", dupa relevanta in fisierul " + mDir +
                " are urmatoarele rezultate:");
        for (Map.Entry<Integer, String> entry : sorted.entrySet()) {
            System.out.println(i + ". " + entry.getValue());
            i++;
        }
    }

    /**
     * Functia de cautare recursiva in root_dir
     */
    public void searchInTextFiles(String mKey, File dir) {
        File[] a = dir.listFiles();
        for (File f : a) {
            if (f.isDirectory())
                searchInTextFiles(mKey, f);
            else
                searchInFile(mKey, f);
        }
    }

    /**
     * Functia de cautare a cuvantului cheie si numararea aparitiei acestuia in fiecare URL
     */
    private void searchInFile(String key, File f) {
        int count = 0;
        try {
            FileInputStream fstream = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                int startIndex = strLine.indexOf(key);
                while (startIndex != -1) {
                    count++;
                    startIndex = strLine.indexOf(key,
                            startIndex + key.length());
                }
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
            Log.getInstance().writeToFile("Warning : " + e.getMessage() + "!\n");
        }
        if (count > 0)
            urls.put(new Integer(count), f.toString()); //inserarea URL-ului in map
    }
}
