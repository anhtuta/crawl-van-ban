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
            String filePath = folder + File.separator + savedFilename;
            downloadAndSave(urlPath, filePath);
        } catch (MalformedURLException e1) {
            System.out.println("Error: url is not valid: " + e1.getMessage());
        }
    }

    public static void downloadFileFromUrl(String url, String folder) {
        downloadFileFromUrl(url, folder, null);
    }

    public static void downloadFileFromUrlWithParams(String url, String folder, String extension,
            String filename) {
        System.out.println("[downloadFileFromUrl]: url = " + url);

        try {
            File directory = new File(folder);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // ex: url =
            // https://preview.redd.it/8y3tt1ah9dq91.png?width=4251&format=png&auto=webp&s=017f51ded8fbf94583a00bb608356d6f879fe0c2
            URL urlObj = new URL(url);
            String urlPath = urlObj.getPath();
            String savedFilename = filename != null
                    ? filename + "." + extension
                    : urlPath.substring(urlPath.lastIndexOf("/") + 1, urlPath.indexOf("?"));
            String filePath = folder + File.separator + savedFilename;
            downloadAndSave(url, filePath);
        } catch (MalformedURLException e1) {
            System.out.println("Error: url is not valid: " + e1.getMessage());
        }
    }

    private static void downloadAndSave(String url, String filePath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("\tDownloaded and saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Download error: " + e.getMessage());
        }
    }

}
