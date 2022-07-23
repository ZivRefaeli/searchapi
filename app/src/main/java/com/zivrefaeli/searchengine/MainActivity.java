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
    private TextView tvTotalResults;
    private EditText etKeyword;
    private SearchZ searchZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivResult = findViewById(R.id.iv_result);
        tvTotalResults = findViewById(R.id.tv_total_results);
        etKeyword = findViewById(R.id.et_keyword);
        searchZ = new SearchZ(this, API_KEY, CX);
    }

    public void search(View view) {
        String keyword = etKeyword.getText().toString();

        searchZ.search(keyword, (image, totalResults) -> {
            ivResult.setImageBitmap(image);
            tvTotalResults.setText(String.format("Total Results: %s", totalResults));
            etKeyword.setText(null);
        });
    }
}