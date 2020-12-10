package Crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindURL {

    private List<String> pagesToDownload;

    public FindURL(){
        pagesToDownload = new ArrayList<>();
    }

    public void attribute(String link, String protocol, String host, String path){
        String linkHref = link;
        String finalHref;

        if (linkHref.length()>2 && linkHref.substring(0,1).equals("/") && !linkHref.substring(0,2).equals("//"))
        {
            finalHref = protocol + "://" + host + linkHref;
            //System.out.println(finalHref);
            //System.out.println(linkHref);
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>2 && linkHref.substring(0,2).equals("//"))
        {
            finalHref = protocol + ":" + linkHref;
            //System.out.println(finalHref);
            //System.out.println(linkHref);
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>2 && linkHref.substring(0,2).equals("./"))
        {
            path = path.substring(0,path.lastIndexOf("/"));
            finalHref = protocol + "://" + host + path + linkHref.substring(1);
            //System.out.println(finalHref);
            //System.out.println(linkHref);
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>1 && linkHref.substring(0,1).equals("#"))
        {
            finalHref = protocol + "://" + host + path + linkHref;
            //System.out.println(finalHref);
            //System.out.println(linkHref);
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>3 && linkHref.substring(0,3).equals("../"))
        {
            finalHref = protocol + "://" + host + '/' + linkHref;
            //System.out.println(finalHref);
            //System.out.println(linkHref);
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>5 && !linkHref.substring(0,5).equals("https") && !linkHref.substring(0,4).equals("http"))
        {
            finalHref = protocol + "://" + host + '/' + linkHref;
            //System.out.println(finalHref);
            //System.out.println(linkHref);
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>5 && linkHref.substring(0,5).equals("https"))
        {
            finalHref = linkHref;
            //System.out.println(finalHref);
            //System.out.println(linkHref);
            pagesToDownload.add(finalHref);
        }
        else if (linkHref.length()>4 && linkHref.substring(0,5).equals("http"))
        {
            finalHref = linkHref;
            //System.out.println(finalHref);
            //System.out.println(linkHref);
            pagesToDownload.add(finalHref);
        }
    pagesToDownload = pagesToDownload.stream().distinct().collect(Collectors.toList());  // eliminare duplicate
    }

    public List<String> Get_List_Pages_To_Download() {
        return pagesToDownload;
    }

    public void Add_List_Pages_To_Download(List<String> allPagesToDownload) {
        pagesToDownload.clear();
        pagesToDownload.addAll(allPagesToDownload);
    }
}
