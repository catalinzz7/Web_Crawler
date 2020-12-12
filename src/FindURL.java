import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classname FindURL
 * Implementeaza clasa FindURL care se ocupa
 * cu formarea cailor tuturor link-urilor in
 * functie de pagina curenta
 *
 * @author Dan-Cristian Gutiu
 */
public class FindURL {
    /**
     * Declararea membrilor
     */
    private List<String> pagesToDownload;

    /**
     * Constructor de clasa FindURL
     * Initializeaza o lista de pagini
     */
    public FindURL(){
        pagesToDownload = new ArrayList<>();
    }

    public void attribute(String link, String protocol, String host, String path){
        String linkHref = link;
        String finalHref;

        /* formare URL-uri in functie de URL-ul curent */
        if (linkHref.length()>2 && linkHref.substring(0,1).equals("/") && !linkHref.substring(0,2).equals("//"))
        {
            finalHref = protocol + "://" + host + linkHref;
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>2 && linkHref.substring(0,2).equals("//"))
        {
            finalHref = protocol + ":" + linkHref;
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>2 && linkHref.substring(0,2).equals("./"))
        {
            path = path.substring(0,path.lastIndexOf("/"));
            finalHref = protocol + "://" + host + path + linkHref.substring(1);
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>1 && linkHref.substring(0,1).equals("#"))
        {
            finalHref = protocol + "://" + host + path + linkHref;
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>3 && linkHref.substring(0,3).equals("../"))
        {
            finalHref = protocol + "://" + host + '/' + linkHref;
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>5 && !linkHref.substring(0,5).equals("https") && !linkHref.substring(0,4).equals("http"))
        {
            finalHref = protocol + "://" + host + '/' + linkHref;
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>5 && linkHref.substring(0,5).equals("https"))
        {
            finalHref = linkHref;
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>4 && linkHref.substring(0,5).equals("http"))
        {
            finalHref = linkHref;
            pagesToDownload.add(finalHref);
        }

        /* eliminarea duplicatelor */
        pagesToDownload = pagesToDownload.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Functie Get_List_Pages_To_Download()
     * Returneaza o lista de pagini pentru descarcare
     */
    public List<String> Get_List_Pages_To_Download() {
        return pagesToDownload;
    }

    /**
     * Functie Add_List_Pages_To_Download()
     * @param allPagesToDownload Pagini pentru descarcare
     */
    public void Add_List_Pages_To_Download(List<String> allPagesToDownload) {
        pagesToDownload.clear();
        pagesToDownload.addAll(allPagesToDownload);
    }
}
