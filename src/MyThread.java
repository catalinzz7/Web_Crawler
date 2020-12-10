import java.util.Vector;
import java.lang.Math;

class MyThread extends Thread {
    private Thread t;
    private String threadName;
    private Vector<String> URLs;
    private Integer Thread_id;
    private Integer log_lvl;
    private String Store_dir;
    private Integer P;

    MyThread( String name, Integer id, Vector<String> urlList, Integer log, String store, Integer p) {
        threadName = name;

        URLs = urlList;
        Thread_id = id;
        log_lvl = log;
        Store_dir = store;
        P=p;
    }

    public void run() {
        Integer N = URLs.size();

        Integer start = (int)(Thread_id * Math.ceil(N/P));
        Integer end= (int)(Math.min(N, (Thread_id+1) * Math.ceil(N/P)));


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

    public void start () {

        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}