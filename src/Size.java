import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Size {
    private int maxim;
    private String root;
    private List<String> fisiere;

    public Size(int minim, int maxim, String root) {
        this.maxim = maxim;
        this.root = root;
    }

    public void run()
    {
        File directory = new File(root);

        File[] fList = directory.listFiles();
        if (fList!=null)
        {
            for(File fisier : fList)
            {
                if(fisier.isFile())
                {
                    if (fisier.length() < maxim)
                        fisiere.add(fisier.toString());
                }
                else if (fisier.isDirectory())
                {
                    this.root = fisier.toString();
                    run();
                }
            }
        }
    }

    public void Printare()
    {
        for (String s : fisiere)
            System.out.println(s);
    }
}
