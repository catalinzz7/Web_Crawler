import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class Log {

    private static Log single_instance = null;
    private FileWriter fisier;

    private Log() {
        try {
            fisier = new FileWriter("../log.txt");
        } catch (IOException e) {
            System.out.println("Nu am putut creea fisierul de log.\n");
            e.printStackTrace();
        }
    }
    public void AdaugareMesaj(String s)
    {
        try {
            DateTimeFormatter data = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String linie = s + "\t" + now;
            fisier.write(linie);
        } catch (IOException e) {
            System.out.println("Nu am putut face linia de output.\n");
            e.printStackTrace();
        }
    }
    public static Log getInstance()
    {
        if (single_instance == null)
            single_instance = new Log();
        return single_instance;
    }
}
