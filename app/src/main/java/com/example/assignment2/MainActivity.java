package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;

public class MainActivity extends AppCompatActivity {

    private TextView time;
    private EditText comments;
    private Button download;

    private long beginTime;
    private long endTime;
    private static final String url = "https://homepages.cae.wisc.edu/~ece533/images/airplane.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.time);
        comments = findViewById(R.id.comment);
        download = findViewById(R.id.update);

        setListeners();
    }

    private void setListeners() {
        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(comments.getText().toString().length() > 0){
                    comments.setBackgroundResource(android.R.color.transparent);
                } else {
                    comments.setBackgroundResource(R.drawable.round_rectangle);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginTime = System.currentTimeMillis();
                downloadImage();
            }
        });
    }

    private void downloadImage() {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);

        requestQueue.start();

        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        endTime = System.currentTimeMillis();
                        long diff = endTime - beginTime;
                        time.setText(diff + "");
                    }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"ERROR!",Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(imgRequest);
    }
}
