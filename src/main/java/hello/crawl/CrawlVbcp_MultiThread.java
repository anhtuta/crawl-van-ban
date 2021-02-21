package hello.crawl;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Crawl van ban chinh phu .pdf files
 * 
 * @author Anhtu
 */
public class CrawlVbcp_MultiThread {

    private static final int START_PAGE = 1;

    private static final int END_PAGE = 2;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = START_PAGE; i <= END_PAGE; i++) {
            String url =
                    "http://vanban.chinhphu.vn/portal/page/portal/chinhphu/hethongvanban?_search=Tìm&_page="
                            + i;
            try {
                Document doc = Jsoup.connect(url).get();
                Elements listLinkElements = doc.select(".doc_list_link");
                DownloadFileRunnable runnable =
                        new DownloadFileRunnable(listLinkElements, i, END_PAGE);
                executor.execute(runnable);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        executor.shutdown();

        // Wait until all threads are finish
        while (!executor.isTerminated()) {
            // Running ...
        }

        System.out.println("Download done!");
    }

}


/**
 * Mỗi 1 thread sẽ download 1 page (chú ý: trong 1 page có nhiều link dẫn tới file pdf)
 */
class DownloadFileRunnable implements Runnable {

    // Trang mà thread này sẽ download
    private int currPage;

    private int totalPage;

    private Elements listLinkElements;

    private static final String DOWNLOAD_FOLDER = "D:/Documents/Others/van-ban/";

    public DownloadFileRunnable(Elements listLinkElements, int currPage, int totalPage) {
        this.listLinkElements = listLinkElements;
        this.currPage = currPage;
        this.totalPage = totalPage;
    }

    // Only download pdf file
    private void downloadFileFromUrl(String url) {
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
            } catch (Exception e) {
                System.out.println("Download error: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Start crawling at page " + currPage + "/" + totalPage);

        for (Element ele : listLinkElements) {
            String pdfPage = ele.absUrl("href");
            Document doc2;
            try {
                doc2 = Jsoup.connect(pdfPage).get();
                Elements pdfElements = doc2.select(".doc_detail_file_link");
                for (Element pdf : pdfElements) {
                    downloadFileFromUrl(pdf.absUrl("href"));
                }
            } catch (IOException e) {
                System.out.println("Download error: " + e.getMessage());
            }
        }

        System.out.println("Finish crawling at page " + currPage + "/" + totalPage);
    }
}
