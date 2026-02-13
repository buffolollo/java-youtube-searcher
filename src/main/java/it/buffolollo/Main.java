package it.buffolollo;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        YouTubeSearcher yt = new YouTubeSearcher(1);

        VideoDetails vd = yt.searchByVideoID("9bZkp7q19f0");

        System.out.println(vd.getTitle());

        // ArrayList<VideoDetails> results = new ArrayList<>();

        // String[] videoNames = {
        // "hysteria muse HQ",
        // "Rosanna toto",
        // "africa toto",
        // "all the small things blink 182",
        // "smells like teen spirit nirvana",
        // "sweet child o mine guns n roses",
        // "stairway to heaven led zeppelin",
        // "bohemian rhapsody queen",
        // "back in black acdc",
        // "highway to hell acdc",
        // };

        // int totalTime = 0;

        // for (int i = 0; i < 20; i++) {
        // long startTime = System.currentTimeMillis();

        // results = yt.searchByVideoName(videoNames[i % videoNames.length]);

        // for (VideoDetails vd : results) {
        // System.out.println(i + ". - " + vd.getTitle());

        // }

        // long endTime = System.currentTimeMillis();

        // int finalTime = (int) ((endTime - startTime));

        // System.out.println("Execution time: " + (finalTime) + " ms");

        // totalTime += finalTime;

        // // Sleep for 1 second to avoid overwhelming the server
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        // System.out.println("--------------------------------------------------");
        // }

        // System.out.println("Total execution time: " + totalTime + " ms");
        // System.out.println("Average execution time: " + (totalTime / 20) + " ms");
    }
}
