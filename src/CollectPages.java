import java.util.ArrayList;
import java.util.List;

/**
 * Clasa CollectPages
 * Implementeaza clasa statica CollectPages cu scopul
 * colectarii tuturor paginilor descarcate
 *
 * @author Dan-Cristian Gutiu
 */
public class CollectPages {
    /**
     * Declararea membrilor
     */
    private static List<String> areDownloaded = new ArrayList<>();

    /**
     * Functia add_pages(String pageDownloaded)
     * Implementeaza adaugarea paginii descarcate
     * in lista de pagini
     * @param pageDownloaded Pagina recent descarcata
     */
    static void add_pages(String pageDownloaded)
    {
        areDownloaded.add(pageDownloaded);
    }

    /**
     * Functie Get_List_Existing_Page()
     * Implementeaza returnarea paginilor descarcate
     */
    public static List<String> Get_List_Existing_Page() {
        return areDownloaded;
    }
}
