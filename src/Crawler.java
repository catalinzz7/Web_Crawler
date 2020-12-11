import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class Crawler {

    public static void main(String[] args) {


        try {
            CrawlerManager CM = new CrawlerManager("src/data/config.cnf", "src/data/URLs.txt");

            Vector<String> v = CM.getUrls();
            Integer P = CM.get_ThreadsNumber_();
            Integer log = CM.get_LogLevel_();
            String store = CM.get_Rootdir_();

            //if(args[0].equals("crawl"))
           // {
                //System.out.println("Hello World!");

            for(int i=0;i<P;i++)
            {
                MyThread Th = new MyThread("Thread", i, v, log, store, P);
                Th.start();
            }


           // }

            //else if (args[0].equals("search"))
           // {


           // }

           // else if (args[0].equals("filter"))
          //  {


            //}



        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
