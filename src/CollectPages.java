package Crawler;

import java.util.ArrayList;
import java.util.List;

public class CollectPages {

    private static List<String> areDownloaded = new ArrayList<>();

    static void add_pages(String pageDownloaded)
    {
        areDownloaded.add(pageDownloaded);
    }


    public static List<String> Get_List_Existing_Page() {
        return areDownloaded;
    }
}
