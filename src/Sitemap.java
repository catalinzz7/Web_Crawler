
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Classname Sitemap
 * Implementeaza clasa Sitemap ce are rolul de
 * a crea un sitemap al unui site descarcat in
 * store cu toate documentele si paginile gasite
 *
 * @author Catalin Raceanu
 */
public class Sitemap {
    /**
     * Declararea membrilor
     */
    private String[] type_list = {"docx", "doc", "png", "jpg", "css", "js", "html"};
    private String Root_path;
    private File outFile;
    private FileWriter myWriter;

    /**
     * Constructor de clasa Sitemap
     * @param root_dir directorul unde sunt descarcate paginile
     */
    public Sitemap(String root_dir)
    {
        Root_path = root_dir;

    }

    /**
     * Functie start()
     * Implementeaza listarea site-urilor descarcate
     * Se alege un site
     * Se incepe scrierea in fisier si cautarea documentelor
     *
     */
    public void start() throws FileNotFoundException {
        File file = new File(Root_path);
        System.out.println("Site-uri disponibile: ");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for(String a : directories)
        {
            System.out.println(a);
        }


        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Alegeti un site: ");

        String site = myObj.nextLine();
        String sitePath = Root_path + "/" + site;

        String fName = "src/data/" + site + ".txt";

        Log.getInstance().writeToFile("INFO: Creare sitemap pentru " + site);

        try {
            outFile = new File(fName);
            if (outFile.createNewFile()) {
                System.out.println("File created: " + outFile.getName());
            } else {
                System.out.println("File already exists.");
            }

            myWriter = new FileWriter(fName);
            myWriter.write(site + ":");
            myWriter.write("\n");

        } catch (IOException e) {
            Log.getInstance().writeToFile("ERROR: Eroare creare sitemap - nu s-a gasit fisierul");
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        create(sitePath);

    }


    /**
     * Functie listf()
     * Implementeaza cautarea recursiva in toate
     * subdirectaorele a fisierelor dupa extensie
     * @param directoryName directorul in care se cauta
     * @param s lista in care se salveaza numele
     * @param type extensia fisierelor cautate
     */
    public void listf(String directoryName, List<String> s, String type) {

        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    Path path = Paths.get(file.toString());
                    String name = path.getFileName().toString();
                    int index = name.lastIndexOf('.');
                    if(index > 0) {
                        String extension = name.substring(index + 1);
                        if (extension.equals(type)) {
                            s.add(name);
                            try {
                                myWriter.write("      - " + name);
                                myWriter.write("\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                } else if (file.isDirectory()) {
                    listf(file.toString(), s, type);
                }
            }
    }

    /**
     * Functie create()
     * Se face cautarea pornind de la o lista de extensii
     * si se scrie in fisier
     * @param link calea catre directorul radacina
     */
    public void create(String link)
    {

        try {
            for( String s : type_list) {
                List<String> list = new ArrayList<String>();


                myWriter.write(s + "/");
                myWriter.write("\n");

                listf(link, list, s);
            }

            myWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}


