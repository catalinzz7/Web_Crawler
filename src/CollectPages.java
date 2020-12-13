import java.util.ArrayList;
import java.util.List;

/**
 * Clasa CollectPages
 * Implementeaza clasa singleton CollectPages cu scopul
 * colectarii tuturor paginilor descarcate suportand
 * multithreading
 *
 * @author Dan-Cristian Gutiu
 */
public class CollectPages {
    /**
     * Declararea și inițializarea membrilor
     */
    private static volatile CollectPages INSTANCE = null;
    private List<String> areDownloaded = new ArrayList<>();

    /**
     * Constructor de clasa CollectPages
     */
    private CollectPages(){

    }

    /**
     * Functie getInstance()
     * Implementeaza crearea unei instante sau
     * returnarea acesteia in cazul in care
     * exista
     */
    public static CollectPages getInstance(){
        if (INSTANCE == null){
            synchronized (CollectPages.class){
                if (INSTANCE == null){
                    INSTANCE = new CollectPages();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Functia add_pages(String pageDownloaded)
     * Implementeaza adaugarea paginii descarcate
     * in lista de pagini
     * @param pageDownloaded Pagina recent descarcata
     */
    void add_pages(String pageDownloaded)
    {
        areDownloaded.add(pageDownloaded);
    }

    /**
     * Functie Get_List_Existing_Page()
     * Implementeaza returnarea paginilor descarcate
     */
    public List<String> Get_List_Existing_Page() {
        return areDownloaded;
    }
}
