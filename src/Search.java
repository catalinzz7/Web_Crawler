import java.io.*;
import java.util.*;

public class Search
{
    File mDir;
    Map<Integer, String> urls = new HashMap<Integer, String>();
    String mKey;

    public Search()
    {
    }
    public void search(String key,String dir)
    {
        this.mDir = new File(dir);
        this.mKey = key;
        if(mDir.exists())
        {
            searchInTextFiles(mKey,mDir);
            print();
        }
        else
            System.out.println("Nu exista fisierul!");
    }
    public void print()
    {
        TreeMap< Integer, String> sorted = new TreeMap<>(Collections.reverseOrder());
        sorted.putAll(urls);
        for (Map.Entry<Integer, String> entry : sorted.entrySet())
            System.out.println(entry.getValue());
    }
    public void searchInTextFiles(String mKey,File dir)
    {
        File[] a = dir.listFiles();
        for (File f : a)
        {
            if (f.isDirectory())
                searchInTextFiles(mKey,f);
            else
                searchInFile(mKey,f);
        }
    }

    private void searchInFile(String key,File f)
    {
        int count = 0;
        try
        {
            FileInputStream fstream = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                int startIndex = strLine.indexOf(key);
                while (startIndex != -1)
                {
                    count++;
                    startIndex = strLine.indexOf(key,
                            startIndex +key.length());
                }
            }
            in.close();
        }
        catch (Exception e){
            System.err.println("Eroare: " + e.getMessage());
        }
        if(count>0)
        urls.put(new Integer(count),f.toString());
    }
}
