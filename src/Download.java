package Crawler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Download {
    public void page(String pageToDownload, String StoreFile) {
        try {
            URL url = new URL(pageToDownload);
            String host = url.getHost();
            String path = url.getPath();

            String pathDirectory = null;
            String pathFile = null;

            if (path.isEmpty())
            {
                pathDirectory = StoreFile + "/";
                pathFile = StoreFile + "/" + host;
            }
            else {
                if (path.lastIndexOf("/")==0)
                {
                    pathDirectory = StoreFile + "/" + host + "/";
                    pathFile = StoreFile + "/" + host + "/" + path.substring(1,path.length());
                }
                else if (path.lastIndexOf("/")!=path.length()-1)
                {
                    pathDirectory = StoreFile + "/" + host + path.substring(0,path.lastIndexOf("/")+1);
                    pathFile = pathDirectory + path.substring(path.lastIndexOf("/")+1,path.length());
                }
                else if (path.lastIndexOf("/") == path.length()-1)
                {
                    String newPath = path.substring(0,path.length()-1);
                    pathDirectory = StoreFile + "/" + host + path.substring(0,newPath.lastIndexOf("/")+1);
                    pathFile = pathDirectory + newPath.substring(newPath.lastIndexOf("/")+1,newPath.length());

                }
            }
            pathDirectory = pathDirectory.replaceAll("[\\:*?\"<>|]", "-");
            pathFile = pathFile.replaceAll("[\\:*?\"<>|]", "-");
            String fileName = pathFile.substring(pathFile.lastIndexOf("/")+1,pathFile.length());

            if (!fileName.contains("."))
            {
                pathFile += ".html";
            }

            Path create_dir = Paths.get(pathDirectory);
            //System.out.println(pathDirectory);
            pathFile = pathFile.replace("\\","");
            //System.out.println(pathFile);
            Files.createDirectories(create_dir);  // creaza directoare


            URL url1 = new URL(pageToDownload);
            HttpURLConnection http = (HttpURLConnection) url1.openConnection();
            http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

            if (http.getResponseCode()>0 && http.getResponseCode()<400) { // sa nu primesc invalid http response
                BufferedInputStream in = new BufferedInputStream(http.getInputStream());
                String currentExtension = pathFile.substring(pathFile.lastIndexOf("."), pathFile.length());

                if (currentExtension.equals(".com")) {
                    String nextExtension = ".html";
                    pathFile = pathFile.replaceAll(currentExtension, nextExtension);
                }
                FileOutputStream fos = new FileOutputStream(pathFile);
                BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = in.read(buffer, 0, 1024)) >= 0) {
                    bout.write(buffer, 0, read);
                }
                CollectPages.add_pages(pageToDownload);
                bout.close();
                in.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
