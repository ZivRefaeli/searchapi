package com.zivrefaeli.searchengine;

import android.graphics.Bitmap;

public interface Result {
    void response(Bitmap image, String totalResults);
}