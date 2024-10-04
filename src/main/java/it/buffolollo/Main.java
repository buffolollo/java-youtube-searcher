package it.buffolollo;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        YouTubeSearcher yt = new YouTubeSearcher(1);

        ArrayList<VideoDetails> results = new ArrayList<>();

        results = yt.searchByVideoName("hysteria muse HQ");

        for (VideoDetails vd : results) {
            System.out.println(vd.getTitle());
            System.out.println(vd.getDescription());
            System.out.println(vd.getChannelName());
            System.out.println(vd.getDuration());
            System.out.println(vd.getViews());
            System.out.println(vd.getVideoID());
            System.out.println(vd.getThumbnails());
            System.out.println("Thumbnail: " + vd.getThumbnails()[0].getUrl());

            System.out.println(" ----------------------------- ");
        }

        //

        results = yt.searchByVideoName("50 special lunapop");

        for (VideoDetails vd : results) {
            System.out.println(vd.getTitle());
            System.out.println(vd.getDescription());
            System.out.println(vd.getChannelName());
            System.out.println(vd.getDuration());
            System.out.println(vd.getViews());
            System.out.println(vd.getVideoID());
            System.out.println(vd.getThumbnails());

            System.out.println(" ----------------------------- ");
        }

        //

        results = yt.searchByVideoName("gigolò sfera");

        for (VideoDetails vd : results) {
            System.out.println(vd.getTitle());
            System.out.println(vd.getDescription());
            System.out.println(vd.getChannelName());
            System.out.println(vd.getDuration());
            System.out.println(vd.getViews());
            System.out.println(vd.getVideoID());
            System.out.println(vd.getThumbnails());

            System.out.println(" ----------------------------- ");
        }
    }
}
