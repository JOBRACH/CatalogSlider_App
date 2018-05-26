package com.example.santirubiras.frankestein_camara;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ImageView fullFoto = findViewById(R.id.imageView);
        fullFoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        String seleccionada = getIntent().getStringExtra("seleccionada");


        File imgFile = new File(seleccionada);
        fullFoto.setImageURI(Uri.fromFile(imgFile));
    }
}
