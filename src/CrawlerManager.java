import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class CrawlerManager {
    private String conf_FileName_;
    private String url_FileName_;

    int threadsNumber_;
    int delay_;
    String root_dir_;
    int log_Level_;

    private Vector<String> urls_;
    private Vector<Integer>urls_id_;

    public CrawlerManager(String confileName,String urlsFile) throws FileNotFoundException {
        this.conf_FileName_=confileName;
        this.url_FileName_=urlsFile;
        this.collect_Conf_Elements_();
    }

    public void printFileNames_()
    {
        System.out.println(this.conf_FileName_+"    "+this.url_FileName_);
    }

    public Vector<String>getUrls() throws FileNotFoundException
    {
        String fileContent=new Scanner(new File(this.url_FileName_)).useDelimiter("\\Z").next();
        String[]parts=fileContent.split("\n");
        this.urls_=new Vector<String>(parts.length);
        this.urls_id_=new Vector<Integer>(parts.length);
        int id=0;
        for(int i=0;i<parts.length;i++)
        {
            this.urls_.add(parts[i]);
            this.urls_id_.add(id);
            id++;
        }
        return this.urls_;
    }

    private void collect_Conf_Elements_() throws FileNotFoundException {
        String fileContent=new Scanner(new File(this.conf_FileName_)).useDelimiter("\\Z").next();
        String parts[]=fileContent.split("\n");

        this.threadsNumber_=Integer.parseInt(parts[0].split("=")[1].trim());
        this.delay_=Integer.parseInt(parts[0].split("=")[1].trim());
        this.root_dir_=parts[2].split("=")[1];
        this.log_Level_=Integer.parseInt(parts[3].split("=")[1].trim());
    }

    public String get_Rootdir_() {
        return root_dir_;
    }

    public int get_ThreadsNumber_() {
        return threadsNumber_;
    }

    public int get_Delay_() {
        return delay_;
    }

    public int get_LogLevel_() {
        return log_Level_;
    }

    public void printUrls_()
    {
        for(int i=0;i<this.urls_.size();i++)
        {
            System.out.println(this.urls_id_.get(i) +"  "+ this.urls_.get(i));
        }
    }
}
