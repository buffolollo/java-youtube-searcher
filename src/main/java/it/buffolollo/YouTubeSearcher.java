package it.buffolollo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class YouTubeSearcher {
   private int maxResults;

   public YouTubeSearcher() {
      this(20); // Default to 20
   }

   public YouTubeSearcher(int maxResults) {
      this.maxResults = Math.max(1, maxResults); // Ensure positive value
   }

   public ArrayList<VideoDetails> searchByVideoName(String videoName) {
      ArrayList<VideoDetails> results = new ArrayList<>();
      try {
         Connection session = Jsoup.newSession();
         videoName = URLEncoder.encode(videoName, StandardCharsets.UTF_8.toString());

         // Perform necessary requests
         session.newRequest("https://www.youtube.com")
               .userAgent(
                     "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
               .method(Method.GET).followRedirects(true).execute();

         Connection.Response response = session.newRequest("https://www.youtube.com/results")
               .data("search_query", videoName)
               .followRedirects(true)
               .method(Method.GET).execute();

         return parseResults(response.body(), results);
      } catch (IOException e) {
         return results; // Return empty results on failure
      }
   }

   private ArrayList<VideoDetails> parseResults(String resBody, ArrayList<VideoDetails> results) {
      int startIndex = resBody.indexOf("var ytInitialData");
      if (startIndex == -1)
         return results;

      resBody = resBody.substring(resBody.indexOf('{', startIndex));
      int endIndex = resBody.indexOf("</script>") - 1;
      resBody = resBody.substring(0, endIndex);

      JsonObject jsonObject = JsonParser.parseString(resBody).getAsJsonObject()
            .getAsJsonObject("contents")
            .getAsJsonObject("twoColumnSearchResultsRenderer")
            .getAsJsonObject("primaryContents")
            .getAsJsonObject("sectionListRenderer");

      JsonArray contents = jsonObject.getAsJsonArray("contents");

      for (JsonElement element : contents) {
         JsonObject itemSection = element.getAsJsonObject().getAsJsonObject("itemSectionRenderer");
         if (itemSection != null && itemSection.has("contents")) {
            JsonArray itemArray = itemSection.getAsJsonArray("contents");
            for (JsonElement item : itemArray) {
               JsonObject videoObject = item.getAsJsonObject().getAsJsonObject("videoRenderer");
               if (videoObject != null) {
                  results.add(createVideoDetails(videoObject));
                  if (results.size() >= this.maxResults)
                     return results;
               }
            }
         }
      }
      return results;
   }

   private VideoDetails createVideoDetails(JsonObject obj) {
      String title = Tools.getVideoTitle(obj);
      String description = Tools.getVideoDescription(obj);
      String channelName = Tools.getVideoChannelName(obj);
      Thumbnail[] thumbnails = Tools.getVideoThumbnails(obj);
      long duration = Tools.getVideoDuration(obj);
      long views = Tools.getVideoViews(obj);
      String videoID = Tools.getVideoID(obj);

      return new VideoDetails(title, description, channelName, thumbnails, duration, views, videoID);
   }

   public VideoDetails searchByVideoID(String videoID) {
      return null; // Implement this method based on requirements
   }

   public VideoDetails searchByVideoURL(String videoURL) {
      String videoID = Tools.getVideoIdFromYouTubeUrl(videoURL);
      return videoID != null ? searchByVideoID(videoID) : null;
   }
}
