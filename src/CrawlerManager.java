import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

/**
 * Clasa implementeaza initializarea crawlerului (extrage datele din fisierul de  configurare si cel cu url-uri).
 * URL-urile sunt puse intr-un Vector<String>, iar pentru a le gestiona mai usor avem la dispozitie si intr-un
 * Vector<Int> id-urile corespondente. Fiecare element din fisierul de configurare are un geter implementat.
 *
 * @author Duica Marius
 */

public class CrawlerManager {
    private String conf_FileName_;

   private int threadsNumber_;
    private int delay_;
    private String root_dir_;
    private  int log_Level_;

    private int forError_=0;

    private Vector<String> urls_;
    private Vector<Integer>urls_id_;

    /**
     * Constructorul clasei
     * @param confileName numele fisierului de configurare
     * @throws FileNotFoundException
     */
    public CrawlerManager(String confileName) throws FileNotFoundException {
        this.conf_FileName_=confileName;
        this.collect_Conf_Elements_();
    }


    //functia in care obtinem URL-urile
    public Vector<String>getUrls(String urlsFile) throws FileNotFoundException
    {
        String fileContent = new Scanner(new File(urlsFile)).useDelimiter("\\Z").next();
        if (fileContent.length() == 0) {
            Log.getInstance().writeToFile("Fisierul cu URL-uri este gol!");
            this.forError_ = 1;
            return this.urls_;
        }
        else {
            String[] parts = fileContent.split("\n");

            this.urls_ = new Vector<String>(parts.length);
            this.urls_id_ = new Vector<Integer>(parts.length);
            int id = 0;
            for (int i = 0; i < parts.length; i++) {
                this.urls_.add(parts[i]);
                this.urls_id_.add(id);
                id++;
            }
            return this.urls_;
        }
    }


    //functia in care clasa obtine elementele din fisierul de configurare
    private void collect_Conf_Elements_() throws FileNotFoundException {
        String fileContent = new Scanner(new File(this.conf_FileName_)).useDelimiter("\\Z").next();
        if (fileContent.length() == 0) {
            Log.getInstance().writeToFile("Warning : Fisierul cu URL-uri este gol! ");
        }
        if (fileContent.length() != 0)
        {
            String parts[] = fileContent.split("\n");

            this.threadsNumber_ = Integer.parseInt(parts[0].split("=")[1].trim());
            this.delay_ = Integer.parseInt(parts[0].split("=")[1].trim());
            this.root_dir_ = parts[2].split("=")[1];
            this.log_Level_ = Integer.parseInt(parts[3].split("=")[1].trim());
            if (this.threadsNumber_ == 0 || this.delay_ < 0 || this.root_dir_.length() == 0 || this.log_Level_ < 0) {
                Log.getInstance().writeToFile("Problema la fisierul de configurare");
            }
        }
    }

    public int getThreadsNumber_() {
        return threadsNumber_;
    }

    public int getDelay_() {
        return delay_;
    }

    public String getRoot_dir_() {
        return root_dir_;
    }

    public int getLog_Level_() {
        return log_Level_;
    }

    /**
     * Mai jos sunt geterele elementelor din fisierul de configurare
     */


    public void printUrls_()
    {
        for(int i=0;i<this.urls_.size();i++)
        {
            System.out.println(this.urls_id_.get(i) +"  "+ this.urls_.get(i));
        }
    }
}
