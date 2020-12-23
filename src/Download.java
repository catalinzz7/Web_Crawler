import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classname Download
 * Implementeaza clasa Download care se ocupa de
 * creare directoarelor unde vor fi descarcate paginile
 * si de descarcarea acestora
 *
 * @author Dan-Cristian Gutiu
 */
public class Download {
    /**
     * Functie page(String pageToDownload, String StoreFile)
     * Implementeaza calea unde vor fi stocate paginile
     * in functie de URL-uri lor
     * Se inlocuieste https:// cu StoreFile
     * @param pageToDownload Pagina de descarcat
     * @param StoreFile Fisier de stocare
     */
    public void page(String pageToDownload, String StoreFile) {
        try {
            URL url = new URL(pageToDownload);
            String host = url.getHost();
            String path = url.getPath();
            String pathDirectory = null;
            String pathFile = null;

            /* se formeaza calea */
            if (path.isEmpty() || path.equals("/")) {
                pathDirectory = StoreFile + "/";
                pathFile = StoreFile + "/" + host;
            }
            else {
                if (path.lastIndexOf("/")==0) {
                    pathDirectory = StoreFile + "/" + host + "/";
                    pathFile = StoreFile + "/" + host + "/" + path.substring(1,path.length());
                }
                else if (path.lastIndexOf("/")!=path.length()-1) {
                    pathDirectory = StoreFile + "/" + host + path.substring(0,path.lastIndexOf("/")+1);
                    pathFile = pathDirectory + path.substring(path.lastIndexOf("/")+1,path.length());
                }
                else if (path.lastIndexOf("/") == path.length()-1) {
                    String newPath = path.substring(0,path.length()-1);
                    pathDirectory = StoreFile + "/" + host + path.substring(0,newPath.lastIndexOf("/")+1);
                    pathFile = pathDirectory + newPath.substring(newPath.lastIndexOf("/")+1,newPath.length());

                }
            }
            /* se elimina semnele neacceptate in denumirea directoarelor */
            pathDirectory = pathDirectory.replaceAll("[\\:*?\"<>|]", "-");
            pathFile = pathFile.replaceAll("[\\:*?\"<>|]", "-");

            boolean nameIsToBig = false;

            String folderName = pathFile;
            while(folderName.contains("/")){
                String newName = folderName.substring(folderName.indexOf("/")+1, folderName.length());
                if (newName.contains("/")) {
                    String checkName = newName.substring(0, newName.indexOf("/"));
                    if (checkName.length()>255){
                        nameIsToBig = true;
                        break;
                    }
                    folderName = newName.substring(newName.indexOf("/"), newName.length());
                }
                else{
                    String checkName = newName;
                    if (checkName.length()>255){
                        nameIsToBig = true;
                        break;
                    }
                    folderName = newName.substring(newName.indexOf("/")+1, newName.length());
                }
            }
     //       boolean exist = false;
       //     Path path1 = Paths.get(pathDirectory);
      //      if (Files.exists(path1)){
      //          exist = true;
       //     }

           if(!nameIsToBig) {
                /* se extrage numele fisierului */
                String fileName = pathFile.substring(pathFile.lastIndexOf("/") + 1, pathFile.length());

                if (!fileName.contains(".")) {
                    pathFile += ".html";
                }

                /* se creeaza directoarele */
                Path create_dir = Paths.get(pathDirectory);
                Files.createDirectories(create_dir);

                pathFile = pathFile.replace("\\", "");
                URL url1 = new URL(pageToDownload);

                /* server redirect - bug fixed */
                HttpURLConnection.setFollowRedirects(false);

                HttpURLConnection http = (HttpURLConnection) url1.openConnection();
                http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11" +
                        " (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

                if (http.getResponseCode() > 0 && http.getResponseCode() < 400) {
                    BufferedInputStream in = new BufferedInputStream(http.getInputStream());
                    String currentExtension = pathFile.substring(pathFile.lastIndexOf("."), pathFile.length());

                    /* vector de Stringuri cu extensii ce nu trebuie modificate */
                    String[] doNotModifyExtension = {".js", ".css", ".png", ".svg", ".gif", ".jpg", ".jpeg", ".ico"};
                    boolean modifyExtension = true;

                    /* verificare daca fisierul curent contine o extensie ce nu trebuie modificata */
                    for (String acceptedExtension : doNotModifyExtension) {
                        if (acceptedExtension.contains(currentExtension)) {
                            modifyExtension = false;
                            break;
                        }
                    }

                    /* modificare extensie */
                    if (modifyExtension) {
                        String nextExtension = ".html";
                        if (!currentExtension.equals(nextExtension)) {
                            pathFile = pathFile.replaceAll(currentExtension, nextExtension);
                        }
                    }

                    FileOutputStream fos = new FileOutputStream(pathFile);
                    BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                    byte[] buffer = new byte[1024];
                    int read = 0;

                    /* descarcarea pagina */
                    while ((read = in.read(buffer, 0, 1024)) >= 0) {
                        bout.write(buffer, 0, read);
                    }
                    Log.getInstance().writeToFile("INFO: Am descarcat pagina " + pageToDownload + " !\n");

                    /* adaugare pagina in lista de pagini descarcate */
                    CollectPages collectPages = CollectPages.getInstance();
                    collectPages.add_pages(pageToDownload);

                    bout.close();
                    in.close();
                } else {
                    Log.getInstance().writeToFile("WARNING: Pagina " + pageToDownload +
                            " nu a putut fi descarcata ! Messaj de eroare " + http.getResponseCode() + " !\n");
                }
            }else{
                Log.getInstance().writeToFile("WARNING: Pagina " + pageToDownload +
                        " nu a putut fi descarcata ! Numele fisierului este prea mare !\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
