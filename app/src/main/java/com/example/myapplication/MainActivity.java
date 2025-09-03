package com.example.myapplication;

import android.content.Intent;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
private Button photo_button;
private ImageView view_finder;
private String currentPhotoPath;
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
        Bitmap image = BitmapFactory.decodeFile(currentPhotoPath);
        view_finder.setImageBitmap(image);
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .edit()
                .putString("last_photo_path", currentPhotoPath)
                .apply();
        Log.d("MainActivity", "Photo taken and displayed");
    }

}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    photo_button = findViewById(R.id.photo_button);
    view_finder = findViewById(R.id.view_finder);
    String lastPhotoPath = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            .getString("last_photo_path", null);

    if (lastPhotoPath != null) {
        File file = new File(lastPhotoPath);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(lastPhotoPath);
            view_finder.setImageBitmap(bitmap);
        }
    }
    photo_button.setOnClickListener(new View.OnClickListener() {public void onClick(View v){dispatchTakePictureIntent();
        Log.d("MainActivity", "Button clicked");
    }});
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });
}
}