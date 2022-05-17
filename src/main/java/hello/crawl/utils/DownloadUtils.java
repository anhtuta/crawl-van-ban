package hello.crawl.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUtils {

    public static void downloadFileFromUrl(String url, String folder, String filename) {
        System.out.println("[downloadFileFromUrl]: url = " + url);

        try {
            File directory = new File(folder);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // ex: url = https://abc.com/haha/huhu12345.jpg?width=968&format=jpg
            URL urlObj = new URL(url);
            String urlPath = urlObj.getPath();  // ex: /haha/huhu12345.jpg
            String extension = urlPath.substring(urlPath.lastIndexOf(".") + 1); // ex: jpg
            String savedFilename = filename != null
                    ? filename + "." + extension
                    : urlPath.substring(urlPath.lastIndexOf("/") + 1);

            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                    FileOutputStream fileOutputStream =
                            new FileOutputStream(folder + File.separator + savedFilename)) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                System.out.println("\tDownloaded: " + savedFilename);
            } catch (IOException e) {
                System.out.println("Download error: " + e.getMessage());
            }
        } catch (MalformedURLException e1) {
            System.out.println("Error: url is not valid: " + e1.getMessage());
        }
    }

    public static void downloadFileFromUrl(String url, String folder) {
        downloadFileFromUrl(url, folder, null);
    }

}
