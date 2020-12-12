import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Classname Crawler
 * Implementeaza clasa Crawler ce constituie
 * clasa main a proiectului, implementand mecanismul
 * de interpretare a argumentelor transmise
 * la executarea programului
 *
 * @author Catalin Raceanu
 */
public class Crawler {

    public static void main(String[] args) {
        /**
         * Informatii privind executia proiectului
         */
        System.out.println("Alegeti una dintre comenzile disponibile:");
        System.out.println("1. Descarcare pagini: crawler crawl <config_file> <urls_file>");
        System.out.println("2. Cautare cuvant cheie: crawler search <key_word> <config_file>");
        System.out.println("3. Filtrare documente: crawler filter type <file_type> <config_file>");
        System.out.println("                       crawler filter size <max_size> <config_file>");
        System.out.println("4. Creare sitemap: crawler sitemap <config_file>");

        try {
            /**
             * Apelul functiilor pentru optiunea
             * de descarcare recursiva a paginilor
             */
            if(args[0].equals("crawl"))
            {
                CrawlerManager CM = new CrawlerManager(args[1]);

                Vector<String> v = CM.getUrls(args[2]);
                Integer P = CM.get_ThreadsNumber_();
                Integer log = CM.get_LogLevel_();
                String store = CM.get_Rootdir_();
                String store2 = store.substring(0, store.length() - 1);

                for(int i=0;i<P;i++)
                {
                    MyThread Th = new MyThread("Thread", i, v, log, store2, P);
                    Th.start();
                }

            }


            /**
             * Apelul functiilor pentru optiunea
             * de cautare a unui cuvant cheie
             */
            else if (args[0].equals("search"))
            {
                CrawlerManager CM = new CrawlerManager(args[2]);
                String store = CM.get_Rootdir_();
                String store2 = store.substring(0, store.length() - 1);

                Search src = new Search();
                src.search(args[1], store2);

            }


            /**
             * Apelul functiilor pentru optiunea
             * de filtrare a documentelor descarcate
             */
            else if (args[0].equals("filter"))
            {
                CrawlerManager CM = new CrawlerManager(args[3]);
                String store = CM.get_Rootdir_();
                String store2 = store.substring(0, store.length() - 1);
                /**
                 * Filtrare dupa extensie
                 */
                if (args[1].equals("type"))
                {
                    FilterType filt1 = new FilterType(store2, args[2]);
                    filt1.run();
                    filt1.Printare();
                }
                /**
                 * Filtrare dupa dimensiunea maxima
                 */
                if (args[1].equals("size"))
                {
                    Size filt2= new Size(0, Integer.parseInt(args[2]),store);
                    filt2.run();

                }

            }

            /**
             * Apelul functiilor pentru optiunea
             * de creare a unui sitemap
             */
            else if (args[0].equals("sitemap"))
            {
                CrawlerManager CM = new CrawlerManager(args[1]);
                String store = CM.get_Rootdir_();
                String store2 = store.substring(0, store.length() - 1);

                Sitemap s = new Sitemap(store2);
                s.start();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
