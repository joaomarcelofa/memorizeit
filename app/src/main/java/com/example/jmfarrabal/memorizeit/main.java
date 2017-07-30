package com.example.jmfarrabal.memorizeit;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

public class main extends AppCompatActivity {

    // Data Structures
    HashMap<Integer,String> sortedObjects; // key = ID, Value = Object Name
    HashMap<Integer,String> guesses; // Used for compare

    // Layout Components
    private FrameLayout imageFrameLayout;
    private TextView imageNameTextView;
    private TextView imageNumberTextView;

    // I need to think about a name
    private int currentProgress;

    // Constants
    private final int MAX = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initialize();
        reset();
        //Declarando variáveis para carregar o layout da Imagem
        // reset();
        // loadNextObject();
        // if isFinished -
            // tryGuess until all guesses are corrects !
    }


    private void initialize(){
        sortedObjects = new HashMap<Integer, String>();
        guesses       = new HashMap<Integer, String>();

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextImage();
            }
        });

        imageNameTextView = (TextView)findViewById(R.id.imageNameTextView);
        imageNumberTextView= (TextView)findViewById(R.id.imageNumberTextView);
    }

    private void reset(){

        sortedObjects.clear();
        guesses.clear();
        AssetManager assets = getAssets();
        currentProgress = 1;

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setText("Next");

        try{
            String[] imageNames = assets.list("object_images");
            Random index = new Random();
            int validImages = 0;

            do{
                int randomIndex = index.nextInt(imageNames.length);
                String sortedImage = imageNames[randomIndex];

                if(!sortedObjects.containsValue(sortedImage)){
                    ++validImages;
                    sortedObjects.put(validImages,sortedImage);
                    Log.i("SUCESS", "IMAGE :" + sortedImage + " " + " / " + String.valueOf(validImages));
                }

            }while(validImages != MAX);

            Log.i("SUCESS", "Task completed !");
        }
        catch(IOException ex) {
            Log.e("DATA ERROR", "Data error on open resource Location");
        }

        loadNextImage();
    }

    private void loadNextImage(){

        AssetManager asset = getAssets();
        InputStream stream;

        imageFrameLayout = (FrameLayout) findViewById(R.id.imageFrameLayout);
        ImageView objectImageView = imageFrameLayout.findViewById(R.id.randomImageView);

        String currentImageName = sortedObjects.get(currentProgress);

        try{
            // Opening and setting the image
            stream = asset.open("object_images/" + currentImageName);
            Drawable image = Drawable.createFromStream(stream,currentImageName);
            objectImageView.setImageDrawable(image);

            // Changing the Image Name and Image number in TextView
            imageNameTextView.setText(currentImageName.substring(0,currentImageName.indexOf('.')));
            imageNumberTextView.setText("Image " + String.valueOf(currentProgress) + " of " + String.valueOf(MAX));
        }
        catch(IOException e){
            Log.e("ERROR !!" , "Erro de carregamento !", e);
        }
    }

    private void nextImage(){
        currentProgress++;
        if(currentProgress < MAX){
            loadNextImage();
        }
            else if (currentProgress == MAX){
                loadNextImage();
                Button nextButton = (Button) findViewById(R.id.nextButton);
                nextButton.setText("Finish");
            }
                else{
                    reset();
                }
    }

}
