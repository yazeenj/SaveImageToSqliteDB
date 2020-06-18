package com.example.imagecapture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btnTakePicture;
    private DBHelper myDBHelper;
    private ArrayList<Bitmap> images;
    private ConstraintLayout my_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        btnTakePicture = findViewById(R.id.btn_takepic);
        myDBHelper = new DBHelper(this);
        my_layout = findViewById(R.id.my_layout);





        images = myDBHelper.getAllImages();

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,50);
            }
        });

        btnTakePicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Bitmap img = myDBHelper.getImage(4);
                imageView.setImageBitmap(images.get(1));
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 50){
            if(resultCode == RESULT_OK){
                Bitmap bitmapImage = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmapImage);
                try {
                    boolean status = myDBHelper.addImageToDB(bitmapImage);
                    Snackbar ss = Snackbar.make(my_layout,"Added image to db" + status, Snackbar.LENGTH_SHORT).setAction("Remove", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "Action Clicked", Toast.LENGTH_SHORT).show();
                            //code
                        }
                    });
                    ss.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
