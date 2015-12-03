package jc.house.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jc.house.models.House;
import jc.house.models.JCActivity;
import jc.house.models.News;

/**
 * Created by hzj on 2015/11/1.
 */
public final class ParseJson {

    public static List<News> parseNews(JSONArray array) {
        List<News> news = new ArrayList<News>();
        if (null == array || array.length() == 0) {
            return news;
        }
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                if (item.has("id") && item.has("picUrl") && item.has("title") && item.has("author") && item.has("time")) {
                    int id = item.getInt("id");
                    String url = item.getString("picUrl");
                    String title = item.getString("title");
                    String author = item.getString("author");
                    String time = item.getString("time");
                    news.add(new News(id, url, title, author, time));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

    public static List<House> parseHouse(JSONArray array) {
        List<House> houses = new ArrayList<House>();
        if (null == array || array.length() == 0) {
            return houses;
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject item = array.getJSONObject(i);
                if (item.has("id") && item.has("url") && item.has("name") && item.has("phone") && item.has("intro") && item.has("lat") && item.has("lng")) {
                    int id = item.getInt("id");
                    String url = item.getString("url");
                    String name = item.getString("name");
                    String phone = item.getString("phone");
                    String intro = item.getString("intro");
                    double lat = item.getDouble("lat");
                    double lng = item.getDouble("lng");
                    houses.add(new House(id, url, name, intro, phone, lat, lng));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return houses;
    }

    public static List<JCActivity> parseActivity(JSONArray array) {
        List<JCActivity> activities = new ArrayList<JCActivity>();
        if (null == array || array.length() == 0) {
            return activities;
        }
        for (int i = 0; i< array.length(); i++) {
            try {
                JSONObject item = array.getJSONObject(i);
                if (item.has("id") && item.has("url") && item.has("name")) {
                    int id = item.getInt("id");
                    String url = item.getString("url");
                    String name = item.getString("name");
                    activities.add(new JCActivity(id, url, name));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return activities;
    }
}
