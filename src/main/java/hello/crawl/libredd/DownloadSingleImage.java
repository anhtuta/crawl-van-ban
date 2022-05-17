package hello.crawl.libredd;

import hello.crawl.utils.DownloadUtils;

/**
 * A thread to download single image from URL
 * 
 * @author tu.ta.anh
 */
public class DownloadSingleImage implements Runnable {

    private String url;
    private String folder;
    private String filename;

    public DownloadSingleImage(String url, String folder, String filename) {
        super();
        this.url = url;
        this.folder = folder;
        this.filename = filename;
    }

    @Override
    public void run() {
        DownloadUtils.downloadFileFromUrl(url, folder, filename);
    }

}
