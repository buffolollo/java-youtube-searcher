package it.buffolollo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Tools {
    // Consolidate common JSON array extraction logic into a helper method.
    private static JsonArray getJsonArray(JsonObject obj, String key) {
        JsonElement element = obj.get(key);
        if (element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        return new JsonArray(); // Return an empty array if not present or not an array
    }

    protected static String getVideoTitle(JsonObject jsonObject) {
        try {
            return jsonObject.getAsJsonObject("title")
                    .getAsJsonArray("runs").get(0)
                    .getAsJsonObject().get("text").getAsString();
        } catch (Exception e) {
            return ""; // Consider logging the exception
        }
    }

    protected static String getVideoDescription(JsonObject jsonObject) {
        StringBuilder sb = new StringBuilder();
        for (JsonElement element : getJsonArray(jsonObject, "detailedMetadataSnippets")) {
            JsonObject snippet = element.getAsJsonObject();
            sb.append(snippet.getAsJsonObject("snippetText")
                    .getAsJsonArray("runs").get(0)
                    .getAsJsonObject().get("text").getAsString())
                    .append("\n");
        }
        return sb.toString();
    }

    protected static String getVideoChannelName(JsonObject jsonObject) {
        try {
            return jsonObject.getAsJsonObject("ownerText")
                    .getAsJsonArray("runs").get(0)
                    .getAsJsonObject().get("text").getAsString();
        } catch (Exception e) {
            return ""; // Consider logging the exception
        }
    }

    protected static Thumbnail[] getVideoThumbnails(JsonObject jsonObject) {
        ArrayList<Thumbnail> thumbnails = new ArrayList<>();

        if (jsonObject.has("thumbnail")) {
            JsonObject thumbnailObject = jsonObject.getAsJsonObject("thumbnail");
            if (thumbnailObject.has("thumbnails")) {
                JsonArray thumbnailArray = thumbnailObject.getAsJsonArray("thumbnails");
                for (JsonElement element : thumbnailArray) {
                    JsonObject o = element.getAsJsonObject();
                    thumbnails.add(new Thumbnail(o.get("height").getAsInt(),
                            o.get("width").getAsInt(),
                            o.get("url").getAsString()));
                }
            }
        }

        return thumbnails.toArray(new Thumbnail[0]);
    }

    protected static long getVideoDuration(JsonObject jsonObject) {
        try {
            String[] timeSplit = jsonObject.getAsJsonObject("lengthText")
                    .get("simpleText").getAsString().split(":");
            int hours = timeSplit.length == 3 ? Integer.parseInt(timeSplit[0]) : 0;
            int minutes = Integer.parseInt(timeSplit[timeSplit.length - 2]);
            int seconds = Integer.parseInt(timeSplit[timeSplit.length - 1]);
            return hours * 3600 + minutes * 60 + seconds;
        } catch (Exception e) {
            return 0L; // Consider logging the exception
        }
    }

    protected static long getVideoViews(JsonObject jsonObject) {
        try {
            String views = jsonObject.getAsJsonObject("viewCountText")
                    .get("simpleText").getAsString().split(" ")[0]
                    .replace(".", "").replace(",", "").trim();
            return Long.parseLong(views);
        } catch (Exception e) {
            return 0L; // Consider logging the exception
        }
    }

    protected static String getVideoID(JsonObject jsonObject) {
        return jsonObject.has("videoId") ? jsonObject.get("videoId").getAsString() : "";
    }

    protected static String getVideoIdFromYouTubeUrl(String ytUrl) {
        try {
            URL urlObj = new URI(ytUrl).toURL();
            String[] params = urlObj.getQuery().split("&");
            for (String param : params) {
                String[] kv = param.split("=");
                if (kv.length == 2 && "v".equals(URLDecoder.decode(kv[0], StandardCharsets.UTF_8))) {
                    return URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                }
            }
        } catch (MalformedURLException | URISyntaxException e) {
            // Consider logging the exception
        }
        return null;
    }
}
