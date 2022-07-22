package com.zivrefaeli.searchengine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String API_KEY = "API_KEY";
    public static final String CX = "CX";
    private ImageView ivResult;
    private TextView tvSearches;
    private EditText etKeyword;
    private SearchEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivResult = findViewById(R.id.iv_result);
        tvSearches = findViewById(R.id.tv_searches);
        etKeyword = findViewById(R.id.et_keyword);
        engine = new SearchEngine(this, API_KEY, CX);
    }

    public void search(View view) {
        String keyword = etKeyword.getText().toString();
        engine.search(keyword, (image, searches) -> {
            ivResult.setImageBitmap(image);
            tvSearches.setText(String.format("Searches: %s", searches));
        });
        etKeyword.setText(null);
    }
}