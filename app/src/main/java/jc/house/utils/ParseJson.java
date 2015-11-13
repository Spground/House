package jc.house.utils;

import android.graphics.LinearGradient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jc.house.models.ChatUser;
import jc.house.models.House;
import jc.house.models.News;

/**
 * Created by hzj on 2015/11/1.
 */
public final class ParseJson {
    private static final int CODE_SUCCESS = 1;
    public static List<News> parseChatUser(JSONArray array) {
        List<News> result = new ArrayList<News>();
        try {
            for(int i =0; i< array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                int id = item.getInt("id");
                String url = item.getString("url");
                String title = item.getString("title");
                String author = item.getString("author");
                String date = item.getString("date");
                result.add(new News(id, url, title, author, date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<House> parseHouse(JSONArray array) {
        List<House> houses = new ArrayList<House>();
        if (null == array || array.length() == 0) {
            return houses;
        }
        for (int i = 0; i< array.length(); i++) {
            try {
                JSONObject item = array.getJSONObject(i);
                if (item.has("id")){

                }
                int id = item.getInt("id");
                String url = item.getString("url");
                String name = item.getString("name");
                String phone = item.getString("phone");
                String intro = item.getString("intro");
                double lat = item.getDouble("lat");
                double lng = item.getDouble("lng");
                House house = new House(id, url, name, intro, phone, lat, lng);
                houses.add(house);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return houses;
    }

    public static boolean serverResult(JSONObject obj) {
        try {
            if(null != obj && obj.getInt("code") == CODE_SUCCESS) {
                return true;
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
