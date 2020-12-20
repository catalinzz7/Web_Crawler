import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.ClosedWatchServiceException;
import java.util.*;
import java.util.List;

/**
 * Classname Crawl
 * Implementeaza clasa Crawl ce controleaza toata
 * activitatea de descarcare a paginilor, de cautare
 * a URL-urilor, de verificare a fisierului "robots.txt",
 * precum si de descarcare recursiva
 *
 * @author Dan-Cristian Gutiu
 */
public class Crawl implements iCrawl {
    /**
     * Declararea membrilor
     */
    private Robots robots;
    private Download download;
    private FindURL findURL;
    private String PageToDownload;
    private String StoreFile;
    private int SearchLevel;

    /**
     * Constructor de clasa Crawl
     */
    public Crawl(){
        /* initializare elemente clase Robots,Download,FindURL */
        robots = new Robots();
        download = new Download();
        findURL = new FindURL();
    }
    /**
     * Constructor de clasa Crawl
     * @param PageToDownload Pagina de descarcat
     * @param SearchLevel Nivel de cautare
     * @param StoreFile Fisier de stocare
     */
    public Crawl(String PageToDownload, int SearchLevel, String StoreFile){
        /* initializare membrii */
        this.PageToDownload = PageToDownload;
        this.SearchLevel = SearchLevel;
        this.StoreFile = StoreFile;
        download = new Download();
        findURL = new FindURL();

        try {
            /* initializare element url cu numele paginii de descarcat */
            URL url = new URL(PageToDownload);
            /* initializare element robots cu protocul si hostul url-ului */
            robots = new Robots(url.getProtocol(), url.getHost());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Functie check_robots()
     * Implementeaza apelarea elementului robots
     * pentru verificarea fisierului "robots.txt"
     * @return true or false
     */
    @Override
    public boolean check_robots() {
        /* apelare functie verify() din clasa Robots */
        boolean isOK = robots.verify();
        return isOK;
    }

    /**
     * Functie download_page()
     * Implementeaza apelarea elementului download
     * pentru descarcarea paginii
     */
    @Override
    public void download_page() {
        /* apelare functie page() din clasa Download */
        download.page(PageToDownload, StoreFile);
    }

    /**
     * Functie find_URL
     * Implementeaza conectarea la pagina de descarcat,
     * cautarea liniilor relevante(care contin tag-uri
     * specificat in vectorul de stringuri Tags),
     * cautarea atributelor relevante si trimiterea
     * stringului obtinut catre FindURL
     */
    @Override
    public void find_URL() {
        try {
            if (SearchLevel>0) {
                URL url = new URL(PageToDownload);
                String protocol = url.getProtocol();
                String host = url.getHost();
                String path = url.getPath();
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)" +
                        " AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

                if (http.getResponseCode()>0 && http.getResponseCode()<400) {
                    Scanner sc = new Scanner(http.getInputStream());
                    StringBuffer sb = new StringBuffer();
                    /** initializarea vector stringuri cu
                       tag-uri relevante
                     */
                    String[] Tags = {"<link ","<a ","<img "};
                    /**
                     * initializarea vector stringuri cu
                     * atributele relevante
                     */
                    String[] Attributes = {"href","src"};
                    List<String> Lines = new ArrayList<>();

                    /* cautarea tag-urilor pe fiecare linie */
                    while(sc.hasNextLine()) {
                        String line = sc.nextLine();

                        for(String tag:Tags) {
                            String Line = line;

                            /**
                             *  parsarea liniei pentru obtinerea tag-ului
                             *  sub forma "<tag >"
                             *  reinitializare Line cu restul continutului
                             *  ramas
                             */
                            while (Line.contains(tag)) {
                                String interest = Line.substring(Line.indexOf(tag), Line.length());
                                String newLine = interest.substring(0, interest.indexOf(">") + 1);
                                Lines.add(newLine);
                                Line = interest.substring(interest.indexOf(">") + 1, interest.length());
                            }
                        }
                    }

                    /**
                     * cautarea atributelor in lista de linii
                     * ce contin tag-uri relevante
                     */
                    for(String Line:Lines){

                        for(String Attribute:Attributes){

                            if (Line.contains(Attribute)){
                                String attr = Line.substring(Line.indexOf(Attribute),Line.length());

                                /* parsarea liniei pentru obtinerea link-ului */
                                if (attr.contains("\"")){
                                    String content = attr.substring(attr.indexOf("\"")+1,attr.length());

                                    /* bug fixed */
                                    if(content.contains("\"")){
                                    String final_content = content.substring(0,content.indexOf("\""));
                                    findURL.attribute(final_content,protocol,host,path);
                                }
                                }
                            }
                        }
                    }
                }
                else{
                    Log.getInstance().writeToFile("WARNING: Pagina " + PageToDownload +
                            " nu a putut fi accesata ! Messaj de eroare " + http.getResponseCode() + " !\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Functie remove_dissallow_url()
     * Implementeaza eliminarea paginilor ce sunt
     * nepermise de fisierul "robots.txt" din lista
     * paginilor pentru descarcare
     */
    @Override
    public void remove_disallow_url() {
        /* initializare membrii */
        List<String> DisallowURL = robots.Get_List_Disallow_URL();
        List<String> pagesToDownload = findURL.Get_List_Pages_To_Download();
        List<String> allPagesToDownload = new ArrayList<>();

        /* eliminare pagini nepermise */
        for(String page:pagesToDownload){
            for(String disallowPage:DisallowURL){
                if (page.equals(disallowPage)){
                    break;
                }
            }
            allPagesToDownload.add(page);
        }

        findURL.Add_List_Pages_To_Download(allPagesToDownload);
    }

    /**
     * Functie recursive_download()
     * Implementeaza descarcarea recursiva a paginilor,
     * verificarea accesului la pagina, precum si
     * o restrictie in functie de extensia paginilor
     */
    @Override
    public void recursive_download() {
        List<String> pagesToDownload = findURL.Get_List_Pages_To_Download();

        for(String pageToDownload:pagesToDownload){
            iCrawl crawler = new Crawl(pageToDownload, SearchLevel-1, StoreFile);
            /* verificare access pagina */
            boolean iHaveAccess = crawler.check_robots();

            if (iHaveAccess) {
                /* descarcare pagina */
                crawler.download_page();
                boolean isOk = true;

                /**
                 * eliminarea paginilor cu diferite extensii
                 * spre a mai fi accesate pentru cautare de
                 * URL-uri pentru a fi mai tarziu descarcate
                 */
                if (pageToDownload.contains(".")){
                    String Extension = pageToDownload.substring(pageToDownload.lastIndexOf("."),
                            pageToDownload.length());
                    String[] UnsupportedExtensions = {".js",".css",".png",".svg",".gif",".jpg",".jpeg",".ico"};
                    for(String UnsupportedExtension:UnsupportedExtensions){
                        if (UnsupportedExtension.equals(Extension)){
                            isOk = false;
                            break;
                        }
                    }
                }
                if (isOk) {
                    crawler.find_URL();
                    crawler.remove_disallow_url();
                    crawler.remove_existing_page();
                    crawler.recursive_download();
                }
            }
        }

        //findURL.clear();
    }

    /**
     * Functie remove_existing_page()
     * Implementeaza eliminarea paginilor ce au
     * fost deja descarcate spre a mai fi
     * descarcate
     */

    @Override
    public void remove_existing_page() {
        List<String> pagesToDownload = findURL.Get_List_Pages_To_Download();
        List<String> allPagesToDownload = new ArrayList<>();
        CollectPages collectPages = CollectPages.getInstance();

        /**
         * eliminarea din lista de pagini a
         * paginilor descarcate
         */
        for(String page:pagesToDownload){
            for(String existingPage:collectPages.Get_List_Existing_Page()){
                if (page.equals(existingPage)){
                    break;
                }
            }
            allPagesToDownload.add(page);
        }
        findURL.Add_List_Pages_To_Download(allPagesToDownload);
    }
}
