package com.example.santirubiras.frankestein_camara;

import android.animation.Animator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.Toast;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.load.engine.Resource;

import java.io.File;
import java.nio.file.Files;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1; // Solicitud para uso de la camara
    private static final int SELECTED_PIC = 1; //Solicitud para el uso de la gallery
    Button delete;
    boolean deleteMode = false;
    GridView gridView;

    private File[] files;
    private File file[];
    private String[] filesPaths;
    private String[] filesNames;
    private String NameOfFolder = "/Presentacion";


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        delete = findViewById(R.id.trash);
        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);

        showGallery(); //justo al abrir la aplicacion nos pinta la gallery

        //aquí introducimos un setOnClickListener para que cuando cliquemos acceda a la gallery y podamos seleccionar las fotos.
        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_PIC);
                //después del startActivityForResult viene el onActivityResult.
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == SELECTED_PIC && resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(targetUri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filepath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bitmap = BitmapFactory.decodeFile(filepath);

            //Aquí se guarda la imagen. Se llama a la función SaveImage() de la clase Save.java
            Save savefile = new Save();
            savefile.SaveImage(MainActivity.this, bitmap);

            // Llamamos a la función showGallery para que pinte las fotos en el gridView.
            showGallery();

        } else

        // Metodo que enseña la imagen tomada en un imageView
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            Save savefile = new Save();
            savefile.SaveImage(MainActivity.this, bitmap);

            showGallery();
        }

    }

    public void takePhoto(View view) {     // Funcion asociada al boton ImageButton para acceder a la camara del dispositivo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void showGallery(){

        GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(MainActivity.this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(deleteMode){
                    deletePhoto(position);
                } else {
                    viewPhoto(position);

                }
            }

        });

    }

    private void deletePhoto(int position) {
        ImageAdapter ia = new ImageAdapter(MainActivity.this);
        File[] rutasGV = ia.rutas();
        rutasGV[position].delete();
        showGallery();
    }

    private void viewPhoto(int position) {
        ImageAdapter ia = new ImageAdapter(MainActivity.this);
        File[] rutasGV = ia.rutas();
        String seleccionada = rutasGV[position].getAbsolutePath();
        Intent i = new Intent(MainActivity.this, FullScreenImage.class);
        i.putExtra("seleccionada", seleccionada);
        startActivity(i);
    }

    public void imageSwitcher(View v) {
    Intent i = new Intent(this, ImageSwitch.class);
    startActivity(i);

    }

    public void delete(View v){
    if (!deleteMode) {
        v.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed, null));
        Toast.makeText(MainActivity.this, "Haga click en la fotografía para eliminarla.",
                Toast.LENGTH_LONG).show();
        deleteMode =true;
    } else if (deleteMode){
        v.setBackgroundTintList(getResources().getColorStateList(R.color.original, null));
        deleteMode = false;
        }
    }


}
