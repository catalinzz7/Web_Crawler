
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;

public class FilterByType {
    private String extensie;
    private String root;
    private List<String> fisiere;

    public FilterByType(String url, String extensie) {
        this.root = url;
        this.extensie = extensie;
    }


    public void run(){
        File directory = new File(root);

        File[] fList = directory.listFiles();
        if (fList!=null)
        {
            for(File fisier : fList)
            {
                if(fisier.isFile())
                {
                    Path path = Paths.get(fisier.toString());
                    String name = path.getFileName().toString();
                    int index = name.lastIndexOf('.');
                    if (index > 0 )
                    {
                        String ext = name.substring(index+1);
                        if (ext.equals(extensie))
                            fisiere.add(name);
                    }
                }
                else if (fisier.isDirectory())
                {
                    this.root = fisier.toString();
                    run();
                }
            }
        }
    }
    public void Printare(){
        for (String s : fisiere)
            System.out.println(s);
    }
}

