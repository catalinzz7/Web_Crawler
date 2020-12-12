
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.util.Vector;

public class Type {
    private String extensie;
    private String urluri;
    private String[] fisiere;

    public Type(String url, String extensie) {
        this.urluri = url;
        this.extensie = extensie;
    }


    public void run(){
        File files = new File(urluri);
        fisiere = files.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(extensie);
            }
        });
    }
    public void Printare (){
        for (String s : fisiere)
            System.out.println(s);
    }
}
