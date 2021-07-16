package com.smilemakers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.smilemakers.utils.ContextKt.saveData;

public class test extends AppCompatActivity {
    StickerImageView iv_sticker;
    FrameLayout canvas;
    SharedPreferences prefs;

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        ListView lv = findViewById(R.id.lv);
        outState.putLong("key",lv.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);


/*
        canvas = (FrameLayout) findViewById(R.id.vg_canvas);

        ImageView imageView =new ImageView(this);
        //Glide.with(this).load("http://thesmilemakers.in//admin//upload//blog-1.jpg").into(imageView);
//……


        //   try {
        // URL url = new URL("http://thesmilemakers.in//admin//upload//blog-1.jpg");
        //  Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        // add a stickerImage to canvas

        //   } catch(IOException e) {
        //       System.out.println(e);
        //  }

        iv_sticker = new StickerImageView(this);

// add a stickerText to canvas
        StickerTextView tv_sticker = new StickerTextView(this);
        tv_sticker.setText("“call me baby”");
        canvas.addView(tv_sticker);

         prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

       // String bitmap = prefs.getString("test","");
     // iv_sticker.setImageBitmap(StringToBitMap(bitmap));
     //  canvas.addView(iv_sticker);
     //   new MyTask().execute();*/

    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return getBitmapFromURL("http://thesmilemakers.in//admin//upload//blog-1.jpg");

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            iv_sticker.setImageBitmap(bitmap);


// use a default value using new Date()

            prefs.edit().putString("test",bitmap.toString()).apply();
            canvas.addView(iv_sticker);
            super.onPostExecute(bitmap);
        }
    }

    public  Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}




