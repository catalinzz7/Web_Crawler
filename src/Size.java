import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Size {

    private int minim ;
    private int maxim;
    private String url;

    public Size(int minim, int maxim, String url) {
        this.minim = minim;
        this.maxim = maxim;
        this.url = url;
    }

    public void run()
    {
        File files = new File(url);
        String[] fisiere = files.list();
        for (String x : fisiere)
            if (x.length() < maxim && x.length() > minim)
                System.out.println("Fisierul " + x.toString() + " are lungimea de " + x.length() + "MB");

    }
}
