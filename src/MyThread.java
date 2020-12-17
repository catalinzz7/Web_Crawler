import java.util.Vector;
import java.lang.Math;

/**
 * Classname MyThread
 * Implementeaza clasa MyThread ca extensie a
 * clasei Thread pentru implementarea
 * mecanismului de multithreading
 * in procesul de descarcare recursiva
 * a paginilor
 *
 * @author Catalin Raceanu
 */
class MyThread extends Thread {
    /**
     * Declararea membrilor
     */
    private Thread t;
    private String threadName;
    private Vector<String> URLs;
    private Integer Thread_id;
    private Integer log_lvl;
    private String Store_dir;
    private Integer P;

    /**
     * Constructorul clasei MyThread
     * @param name numele threadului
     * @param id Id-ul threadului
     * @param urlList Lista de site-uri
     * @param log nivelul de parcurgere in adancime
     * @param store directorul de stocare
     * @param p numar de thread-uri
     */
    MyThread( String name, Integer id, Vector<String> urlList, Integer log, String store, Integer p) {
        threadName = name;
        URLs = urlList;
        Thread_id = id;
        log_lvl = log;
        Store_dir = store;
        P=p;
    }

    /**
     * Functie run()
     * Implementeaza functia ce va fi executata de
     * fiecare thread in parte
     */
    public void run() {
        Double N = (double)URLs.size();
        Double Pd = (double)P;


        Integer start = (int)(Thread_id* Math.ceil(N/Pd));
        Integer end= (int)(Math.min(N,(Thread_id+1)*Math.ceil(N/Pd)));

        /**
         * Parcurgere paralela a listei de URL-uri
         * si apelul clasei Crawl pentru
         * descarcarea recursiva
         */
        for(int i = start; i < end; i++)
        {

            iCrawl crawler = new Crawl(URLs.get(i), log_lvl, Store_dir);

            boolean iHaveAccess = crawler.check_robots();
            if (iHaveAccess) {
                crawler.download_page();
                crawler.find_URL();
                crawler.remove_disallow_url();
                crawler.remove_existing_page();
                crawler.recursive_download();
            }


        }

    }

    /**
     * Functie start()
     * Implementeaza crearea si initializarea threadurilor
     */
    public void start () {

        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}