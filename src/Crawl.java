package Crawler;

import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.ClosedWatchServiceException;
import java.util.*;
import java.util.List;

public class Crawlr implements iCrawlr {

    private Robots robots;
    private Download download;
    private FindURL findURL;

    private String PageToDownload;
    private String StoreFile;
    private int SearchLevel;

    public Crawlr(){
        robots = new Robots();
        download = new Download();
        findURL = new FindURL();
    }

    public Crawlr(String PageToDownload, int SearchLevel, String StoreFile){
        this.PageToDownload = PageToDownload;
        this.SearchLevel = SearchLevel;
        this.StoreFile = StoreFile;

        download = new Download();
        findURL = new FindURL();

        try {
            URL url = new URL(PageToDownload);
            robots = new Robots(url.getProtocol(), url.getHost());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean check_robots() {
        boolean isOK = robots.verify();
        return isOK;
    }

    @Override
    public void download_page() {
        download.page(PageToDownload, StoreFile);
    }

    @Override
    public void find_URL() {
        try {
            if (SearchLevel>0) {
                URL url = new URL(PageToDownload);
                String protocol = url.getProtocol();
                String host = url.getHost();
                String path = url.getPath();

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

                if (http.getResponseCode()>0 && http.getResponseCode()<400) {
                    Scanner sc = new Scanner(url.openStream());
                    //Instantiating the StringBuffer class to hold the result
                    StringBuffer sb = new StringBuffer();

                    String[] Tags = {"<link ","<a ","<img "};
                    String[] Attributes = {"href","src"};
                    List<String> Lines = new ArrayList<>();

                    while(sc.hasNextLine()) {
                        //sb.append(sc.next());
                        String line = sc.nextLine();
                        //line = line.replace(" ","");

                        for(String tag:Tags) {
                            String Line = line;
                            while (Line.contains(tag)) {
                                String interest = Line.substring(Line.indexOf(tag), Line.length());
                                String newLine = interest.substring(0, interest.indexOf(">") + 1);
                                //System.out.println(newLine);
                                Lines.add(newLine);
                                Line = interest.substring(interest.indexOf(">") + 1, interest.length());
                            }
                        }
                    }

                    for(String Line:Lines){
                        for(String Attribute:Attributes){
                            if (Line.contains(Attribute)){
                                String attr = Line.substring(Line.indexOf(Attribute),Line.length());
                                //System.out.println(attr);
                                if (attr.contains("\"")){
                                    String content = attr.substring(attr.indexOf("\"")+1,attr.length());
                                    String final_content = content.substring(0,content.indexOf("\""));
                                    findURL.attribute(final_content,protocol,host,path);
                                    //System.out.println(final_content);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove_disallow_url() {
        List<String> DisallowURL = robots.Get_List_Disallow_URL();
        List<String> pagesToDownload = findURL.Get_List_Pages_To_Download();
        List<String> allPagesToDownload = new ArrayList<>();

        for(String page:pagesToDownload){
            for(String disallowPage:DisallowURL){
                if (page.equals(disallowPage)){
                    break;
                }
            }
            allPagesToDownload.add(page);
        }

        findURL.Add_List_Pages_To_Download(allPagesToDownload);
    }

    @Override
    public void recursive_download() {
        List<String> pagesToDownload = findURL.Get_List_Pages_To_Download();
        for(String pageToDownload:pagesToDownload){
            //System.out.println(pageToDownload);
            iCrawlr crawler = new Crawlr(pageToDownload, SearchLevel-1, "store");
            boolean iHaveAccess = crawler.check_robots();
            if (iHaveAccess) {
                crawler.download_page();
                boolean isOk = true;
                if (pageToDownload.contains(".")){
                    String Extension = pageToDownload.substring(pageToDownload.lastIndexOf("."),pageToDownload.length());
                    String[] UnsupportedExtensions = {".js",".css",".png",".svg",".gif",".jpg",".jpeg"};
                    for(String UnsupportedExtension:UnsupportedExtensions){
                        if (UnsupportedExtension.equals(Extension)){
                            isOk = false;
                            break;
                        }
                    }
                }
                if (isOk) {
                    crawler.find_URL();
                    crawler.remove_disallow_url();
                    crawler.remove_existing_page();
                    crawler.recursive_download();
                }
            }
        }
    }

    @Override
    public void remove_existing_page() {
        List<String> pagesToDownload = findURL.Get_List_Pages_To_Download();
        List<String> allPagesToDownload = new ArrayList<>();

        for(String page:pagesToDownload){
            for(String existingPage:CollectPages.Get_List_Existing_Page()){
                if (page.equals(existingPage)){
                    break;
                }
            }
            allPagesToDownload.add(page);
        }

        findURL.Add_List_Pages_To_Download(allPagesToDownload);
    }
}
