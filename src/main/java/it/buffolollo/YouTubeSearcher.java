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
   private Connection session = Jsoup.newSession();

   public YouTubeSearcher() {
      this(20); // Default to 20
   }

   public YouTubeSearcher(int maxResults) {
      this.maxResults = Math.max(1, maxResults);

      // Perform necessary requests to establish session
      try {
         session.newRequest("https://www.youtube.com")
               .userAgent(
                     "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
               .method(Method.GET).followRedirects(true).execute();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public ArrayList<VideoDetails> searchByVideoName(String videoName) {
      ArrayList<VideoDetails> results = new ArrayList<>();

      try {
         Connection.Response response = session.newRequest("https://www.youtube.com/results")
               .userAgent(
                     "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
               .data("search_query", URLEncoder.encode(videoName, StandardCharsets.UTF_8.toString()))
               .method(Method.GET)
               .timeout(10000) // 10 seconds
               .maxBodySize(0) // No limit
               .execute();

         return parseResults(response.body(), results);
      } catch (IOException e) {
         return results;
      }
   }

   private ArrayList<VideoDetails> parseResults(String resBody, ArrayList<VideoDetails> results) {
      int startIndex = resBody.indexOf("var ytInitialData");
      if (startIndex == -1) {
         return results;
      }

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
      try {
         Connection.Response response = session.newRequest("https://www.youtube.com/watch")
               .timeout(10000) // 10 seconds
               .maxBodySize(0) // No limit
               .data("v", videoID)
               .followRedirects(true)
               .method(Method.GET).execute();

         // Parse the response to extract video details
         String resBody = response.body();
         int startIndex = resBody.indexOf("var ytInitialPlayerResponse");
         if (startIndex == -1) {
            return null;
         }

         resBody = resBody.substring(resBody.indexOf('{', startIndex));
         int endIndex = resBody.indexOf("};") + 1;
         resBody = resBody.substring(0, endIndex);

         JsonObject jsonObject = JsonParser.parseString(resBody).getAsJsonObject();
         JsonObject videoDetails = jsonObject.getAsJsonObject("videoDetails");

         if (videoDetails == null) {
            return null;
         }

         String title = videoDetails.get("title").getAsString();
         String description = videoDetails.get("shortDescription").getAsString();
         String channelName = videoDetails.get("author").getAsString();
         String videoId = videoDetails.get("videoId").getAsString();

         long views = 0;
         try {
            views = Long.parseLong(videoDetails.get("viewCount").getAsString());
         } catch (NumberFormatException e) {
            e.printStackTrace();
         }

         // Duration is in seconds in this response
         long duration = 0;
         if (jsonObject.has("microformat")) {
            JsonObject microformat = jsonObject.getAsJsonObject("microformat")
                  .getAsJsonObject("playerMicroformatRenderer");
            if (microformat.has("lengthSeconds")) {
               duration = microformat.get("lengthSeconds").getAsLong();
            }
         }

         // Get thumbnails
         Thumbnail[] thumbnails = new Thumbnail[0];
         if (videoDetails.has("thumbnail")) {
            JsonObject thumbnailsObj = videoDetails.getAsJsonObject("thumbnail");
            if (thumbnailsObj.has("thumbnails")) {
               JsonArray thumbnailsArray = thumbnailsObj.getAsJsonArray("thumbnails");

               thumbnails = new Thumbnail[thumbnailsArray.size()];

               for (int i = 0; i < thumbnailsArray.size(); i++) {
                  JsonObject thumbObj = thumbnailsArray.get(i).getAsJsonObject();
                  String url = thumbObj.get("url").getAsString();
                  int width = thumbObj.get("width").getAsInt();
                  int height = thumbObj.get("height").getAsInt();
                  thumbnails[i] = new Thumbnail(height, width, url);
               }
            }
         }

         return new VideoDetails(title, description, channelName, thumbnails, duration, views, videoId);
      } catch (IOException e) {
         return null;
      }
   }

   public VideoDetails searchByVideoURL(String videoURL) {
      String videoID = Tools.getVideoIdFromYouTubeUrl(videoURL);
      return videoID != null ? searchByVideoID(videoID) : null;
   }
}
