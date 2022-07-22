package com.zivrefaeli.searchengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class SearchEngine {
    private static final String TAG = "SearchEngine TAG";
    private static final String url = "https://www.googleapis.com/customsearch/v1";
    private final Context context;
    private final String key, cx;
    private final Handler mainHandler;

    public SearchEngine(@NonNull Context context, String key, String cx) {
        this.context = context;
        this.key = key;
        this.cx = cx;
        mainHandler = new Handler(context.getMainLooper());
    }

    public void search(@NonNull String keyword, Result result) {
        String request = String.format("%s?key=%s&cx=%s&q=%s", url, key, cx, keyword.replace(" ", "%20"));
        Log.i(TAG, "search: request=" + request);

        Volley.newRequestQueue(context).add(new StringRequest(
                Request.Method.GET, request,
                response -> fetch(response, result),
                error -> {
                    error.printStackTrace();
                    mainHandler.post(() -> result.response(null, null));
                }
        ));
    }

    private void fetch(String response, Result result) {
        try {
            int index = 0;
            String src = null;
            JSONObject object = new JSONObject(response);
            JSONArray items = object.getJSONArray("items");

            while (src == null && index < items.length()) {
                JSONObject itemI = (JSONObject) items.get(index);
                if (itemI.has("pagemap")) {
                    JSONObject pageMap = (JSONObject) itemI.get("pagemap");
                    if (pageMap.has("cse_thumbnail")) {
                        JSONObject array = (JSONObject) pageMap.getJSONArray("cse_thumbnail").get(0);
                        src = array.getString("src");
                    }
                }
                index++;
            }

            JSONObject searchInformation = (JSONObject) object.get("searchInformation");
            String searches = searchInformation.getString("formattedTotalResults");
            String finalSrc = src;
            new Thread(() -> {
                try {
                    URL url = new URL(finalSrc);
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    mainHandler.post(() -> result.response(bitmap, searches));
                } catch (IOException e) {
                    e.printStackTrace();
                    mainHandler.post(() -> result.response(null, null));
                }
            }).start();
        } catch (JSONException e) {
            e.printStackTrace();
            mainHandler.post(() -> result.response(null, null));
        }
    }
}
