package hello.crawl;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Crawl van ban chinh phu .pdf files
 * 
 * @author Anhtu
 */
public class CrawlVbcp {

    static final String DOWNLOAD_FOLDER = "D:/Documents/Others/van-ban/";

    static final int START_PAGE = 42;

    static final int END_PAGE = 60;

    // Only download pdf file
    static void downloadFileFromUrl(String url) {
        String extension = url.substring(url.lastIndexOf(".") + 1);

        if ("pdf".equalsIgnoreCase(extension)) {
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            System.out.println("\tDownloaded: " + fileName);
            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                    FileOutputStream fileOutputStream =
                            new FileOutputStream(DOWNLOAD_FOLDER + fileName)) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                System.out.println("Download error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        for (int i = START_PAGE; i <= END_PAGE; i++) {
            System.out.println("Crawling at page " + i + "/" + END_PAGE);
            String url =
                    "http://vanban.chinhphu.vn/portal/page/portal/chinhphu/hethongvanban?_search=Tìm&_page="
                            + i;
            try {
                Document doc = Jsoup.connect(url).get();
                Elements listLinkElements = doc.select(".doc_list_link");

                for (Element ele : listLinkElements) {
                    String pdfPage = ele.absUrl("href");
                    Document doc2 = Jsoup.connect(pdfPage).get();
                    Elements pdfElements = doc2.select(".doc_detail_file_link");
                    for (Element pdf : pdfElements) {
                        // System.out.println(pdf.absUrl("href"));
                        downloadFileFromUrl(pdf.absUrl("href"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
