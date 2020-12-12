import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Size {
    private int maxim;
    private String url;

    public Size(int minim, int maxim, String url) {
        this.maxim = maxim;
        this.url = url;
    }

    public void run()
    {
        File files = new File(url);
        String[] fisiere = files.list();
        for (String fis : fisiere)
            if (fis.length() < maxim)
                System.out.println("Fisierul " + x.toString() + " are lungimea de " + fis.length() + "MB");

    }
}
