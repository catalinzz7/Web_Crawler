import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Classname Robots
 * Implementeaza clasa Robots care incearca
 * sa acceseze fisierul "robots.txt" si sa extraga
 * paginile care nu sunt permise pentru descarcare
 *
 * @author Dan-Cristian Gutiu
 */
public class Robots {
    /**
     * Declararea membrilor
     */
    private String protocolURL;
    private String hostURL;
    private List<String> Disallow_URL;

    /**
     * Constructor de clasa Robots
     * Initializeaza o lista de URL-uri
     */
    public Robots(){
        Disallow_URL = new ArrayList<>();
    }

    /**
     * Constructor de clasa Robots
     * Initializeaza o lista de URL-uri, precum si
     * protocolul si hostul
     */
    public Robots(String protocolURL, String hostURL) {
        this.protocolURL = protocolURL;
        this.hostURL = hostURL;
        Disallow_URL = new ArrayList<>();
    }

    /**
     * Functie verify()
     * Verifica daca fisierul "robots.txt" poate fi
     * accesat,iar daca ii este permis accesul, sa
     * extraga toate URL-urile care sunt nepermise
     * pentru robotul "googlebot"
     */
    public boolean verify() {
        String robots_path = protocolURL + "://" + hostURL + "/robots.txt";
        URL url = null;
        try {
            url = new URL(robots_path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11" +
                    " (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            if (http.getResponseCode()>0 && http.getResponseCode()<400) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                boolean iFind = false;
                boolean iNeedThis = false;
                String line;

                /* citirea liniilor din fisier */
                while ((line = reader.readLine()) != null) {

                    if (line.contains("User-agent:") && iFind == true) {
                        iNeedThis = false;
                        iFind = false;
                    }

                    if (iNeedThis && line.contains("Disallow:")) {
                        for (String val : line.split(" ")) {
                            if (!val.equals("Disallow:")) {
                                String finalURL = protocolURL + "://" + hostURL + val;
                                Disallow_URL.add(val);
                            }
                        }
                        iFind = true;
                    }

                    if (line.toLowerCase().equals("user-agent: googlebot")) {
                        iNeedThis = true;
                    }
                }
                return true;
            }
            if (http.getResponseCode()==404) {
                return  true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* daca pagina nu permite accesul */
        return false;
    }

    /**
     * Functie Get_List_Disallow_URL()
     * Returneaza o lista de URL-uri ce nu
     * nu trebuie accesate
     * @return
     */
    public List<String> Get_List_Disallow_URL() {
        return Disallow_URL;
    }
}
