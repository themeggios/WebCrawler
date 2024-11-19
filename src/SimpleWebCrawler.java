import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class SimpleWebCrawler {

    private Set<String> visitedUrls = new HashSet<>();
    private Queue<UrlDepthPair> urlQueue = new LinkedList<>();

    public void crawl(String seedUrl, int maxDepth) {
        urlQueue.add(new UrlDepthPair(seedUrl, 0));

        while (!urlQueue.isEmpty()) {
            UrlDepthPair currentPair = urlQueue.poll();
            String url = currentPair.url;
            int depth = currentPair.depth;

            if (depth > maxDepth || visitedUrls.contains(url)) {
                continue;
            }

            visitedUrls.add(url);
            System.out.println("Visiting: " + url);

            try {
                Document document = Jsoup.connect(url).get();
                Elements links = document.select("a[href]");

                for (Element link : links) {
                    String absUrl = link.absUrl("href");
                    if (!visitedUrls.contains(absUrl)) {
                        urlQueue.add(new UrlDepthPair(absUrl, depth + 1));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error fetching URL: " + url);
            }
        }
    }

    private static class UrlDepthPair {
        String url;
        int depth;

        UrlDepthPair(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }
    }

    public static void main(String[] args) {
        System.out.print("Inserire URL: ");
        Scanner scanner = new Scanner(System.in);
        String seedURL = scanner.nextLine();
        SimpleWebCrawler crawler = new SimpleWebCrawler();
        crawler.crawl(seedURL, 3);
    }
}