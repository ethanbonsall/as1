package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
private Button photo_button;
private ImageView first_photo;
private ImageView second_photo;
private ImageView third_photo;
private ImageView fourth_photo;
private String currentPhotoPath;
private int n = 0;
private String photosJson;
private static final int REQUEST_IMAGE_CAPTURE = 499;


private File createImageFile() throws IOException {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
    );

    currentPhotoPath = image.getAbsolutePath();
    return image;
}
private void savePhotoPath (String path) {
    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
    photosJson = prefs.getString("photo_paths", "[]");
    try{
        JSONArray oldJsonArray = new JSONArray(photosJson);
        JSONArray JsonArray = new JSONArray();
        JsonArray.put(path);
        for (int i = 0; i < oldJsonArray.length(); i++) {
            JsonArray.put(oldJsonArray.getString(i));
        }
        prefs.edit().putString("photo_paths", JsonArray.toString()).apply();
    } catch (JSONException e) {
        e.printStackTrace();
    }
}

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this,"Image capture failed", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myapplication.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Log.d("MainActivity", "photoFile is null");
            }
        } else {
            Log.d("MainActivity", "No camera activity found!");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            savePhotoPath(currentPhotoPath);
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String photosJson = prefs.getString("photo_paths", "[]");
            try {
                JSONArray jsonArray = new JSONArray(photosJson);

                if (jsonArray.length() > n) {
                    Bitmap bitmap = BitmapFactory.decodeFile(jsonArray.getString(n));
                    first_photo.setImageBitmap(bitmap);
                }

                if (jsonArray.length() > (n+1)) {
                    Bitmap bitmap2 = BitmapFactory.decodeFile(jsonArray.getString((n+1)));
                    second_photo.setImageBitmap(bitmap2);
                }
                if (jsonArray.length() > (n+2)) {
                    Bitmap bitmap3 = BitmapFactory.decodeFile(jsonArray.getString((n+2)));
                    third_photo.setImageBitmap(bitmap3);
                }
                if (jsonArray.length() > (n+3)) {
                    Bitmap bitmap4 = BitmapFactory.decodeFile(jsonArray.getString((n+3)));
                    fourth_photo.setImageBitmap(bitmap4);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("MainActivity", "Photo taken and displayed");
        }
    }

    protected void reorderandRefresh(){
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String photosJson = prefs.getString("photo_paths", "[]");
        try {
            JSONArray jsonArray = new JSONArray(photosJson);

            if (jsonArray.length() > n) {
                Bitmap bitmap = BitmapFactory.decodeFile(jsonArray.getString(n));
                first_photo.setImageBitmap(bitmap);
            }

            if (jsonArray.length() > (n+1)) {
                Bitmap bitmap2 = BitmapFactory.decodeFile(jsonArray.getString((n+1)));
                second_photo.setImageBitmap(bitmap2);
            }
            if (jsonArray.length() > (n+2)) {
                Bitmap bitmap3 = BitmapFactory.decodeFile(jsonArray.getString((n+2)));
                third_photo.setImageBitmap(bitmap3);
            }
            if (jsonArray.length() > (n+3)) {
                Bitmap bitmap4 = BitmapFactory.decodeFile(jsonArray.getString((n+3)));
                fourth_photo.setImageBitmap(bitmap4);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    photo_button = findViewById(R.id.photo_button);
    first_photo = findViewById(R.id.first_photo);
    second_photo = findViewById(R.id.second_photo);
    third_photo = findViewById(R.id.third_photo);
    fourth_photo = findViewById(R.id.fourth_photo);

    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
    String photosJson = prefs.getString("photo_paths", "[]");
    JSONArray jsonArray = null;
    try {
        jsonArray = new JSONArray(photosJson);
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }

    if (jsonArray.length() > 0) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(jsonArray.getString(0));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        first_photo.setImageBitmap(bitmap);
    }
    if (jsonArray.length() > 1) {
        Bitmap bitmap2 = null;
        try {
            bitmap2 = BitmapFactory.decodeFile(jsonArray.getString((1)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        second_photo.setImageBitmap(bitmap2);
    }
    if (jsonArray.length() > 2) {
        Bitmap bitmap3 = null;
        try {
            bitmap3 = BitmapFactory.decodeFile(jsonArray.getString((2)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        third_photo.setImageBitmap(bitmap3);
    }
    if (jsonArray.length() > 3) {
        Bitmap bitmap4 = null;
        try {
            bitmap4 = BitmapFactory.decodeFile(jsonArray.getString((3)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        fourth_photo.setImageBitmap(bitmap4);
    }

    photo_button.setOnClickListener(new View.OnClickListener() {public void onClick(View v){dispatchTakePictureIntent();
        Log.d("MainActivity", "Button clicked");
    }});
    second_photo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            n = n+1;
            reorderandRefresh();
        }
    });
    third_photo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            n = n+2;
            reorderandRefresh();
        }
    });
    fourth_photo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            n = n+3;
            reorderandRefresh();
        }
    });
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });
}
}