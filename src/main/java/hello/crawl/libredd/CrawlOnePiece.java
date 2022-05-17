package hello.crawl.libredd;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlOnePiece {

    private static String url;
    private static int chapter;
    private static final String DOWNLOAD_ROOT_FOLDER = "D:/Documents/OnePiece";

    // Only need to update url and chapter
    private static void init() {
        url = "https://libredd.it/r/OnePiece/comments/tvk01o/chapter_1045_fully_colored/";
        chapter = 1045;

        url = "https://libredd.it/r/OnePiece/comments/tzl7c1/chapter_1046_fully_colored/";
        chapter = 1046;

        url = "https://libredd.it/r/OnePiece/comments/ualp3k/chapter_1047_fully_colored/";
        chapter = 1047;

        url = "https://libredd.it/r/OnePiece/comments/ukibmd/chapter_1048_fully_colored/";
        chapter = 1048;

        url = "https://libredd.it/r/OnePiece/comments/uptirh/chapter_1049_fully_colored/";
        chapter = 1049;
    }

    private static void downloadOnePiece() {
        init();

        if (!url.contains(chapter + "")) {
            System.out.println(
                    "Are you sure? Url and chapter are not match! You should recheck it!");
            return;
        }

        final String DOWNLOAD_FOLDER = DOWNLOAD_ROOT_FOLDER + File.separator + chapter;
        ExecutorService executor = Executors.newFixedThreadPool(10);

        try {
            Document doc = Jsoup.connect(url).get();
            Element gallery = doc.selectFirst(".gallery");
            Elements figures = gallery.select("figure");
            int pageIndex = 1;
            for (Element figure : figures) {
                Element img = figure.selectFirst("a").selectFirst("img");
                String imgName = chapter + "-" + (pageIndex < 10 ? "0" + pageIndex : pageIndex);
                Runnable runnable =
                        new DownloadSingleImage(img.absUrl("src"), DOWNLOAD_FOLDER, imgName);
                executor.execute(runnable);
                pageIndex++;
            }

            executor.shutdown();

            // Wait until all threads are finish
            while (!executor.isTerminated()) {
                // Running ...
            }

            System.out.println("Download done!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        downloadOnePiece();
    }

}
