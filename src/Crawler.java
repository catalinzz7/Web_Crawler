import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Crawler {

    public static void main(String[] args) {
        System.out.println("Alegeti una dintre comenzile disponibile:");
        System.out.println("1. Descarcare pagini: crawler crawl <config_file>");
        System.out.println("2. Cautare cuvant cheie: crawler search <key_word> <config_file>");
        System.out.println("3. Filtrare documente: crawler filter type <file_type> <config_file>");
        System.out.println("                       crawler filter size <max_size> <config_file>");
        System.out.println("4. Creare sitemap: crawler sitemap");

        try {
            CrawlerManager CM = new CrawlerManager("src/data/config.cnf", "src/data/URLs.txt");

            Vector<String> v = CM.getUrls();
            Integer P = CM.get_ThreadsNumber_();
            Integer log = CM.get_LogLevel_();
            String store = CM.get_Rootdir_();
            String store2 = store.substring(0, store.length() - 1);




            if(args[0].equals("crawl"))
            {

                for(int i=0;i<P;i++)
                {
                    MyThread Th = new MyThread("Thread", i, v, log, store2, P);
                    Th.start();
                }


            }



            else if (args[0].equals("search"))
            {
                Search src = new Search();
                src.search(args[1], store2);

            }




            else if (args[0].equals("filter"))
            {
                if (args[1].equals("type"))
                {
                    FilterType filt1 = new FilterType(store2, args[2]);
                    filt1.run();
                    filt1.Printare();
                }

                if (args[1].equals("size"))
                {
                    Size filt2= new Size(0, Integer.parseInt(args[2]),store);
                    filt2.run();

                }

            }

            else if (args[0].equals("sitemap"))
            {
                Sitemap s = new Sitemap(store2);
                s.start();
            }






        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
