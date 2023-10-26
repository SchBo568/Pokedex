package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pokedex.model.Pokemon;
import com.example.pokedex.network.Api;
import com.squareup.picasso.Picasso;

public class PokemonActivity extends AppCompatActivity {

    private Api api = new Api();
    private boolean checkApi = false;
    private Pokemon currentPokemon;
    private String name = "";

    private final Thread secondThread = new Thread(() -> {
        while(true){
            if(checkApi){
                currentPokemon = new Pokemon(name);
                checkApi = false;
            }
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            this.finish();
        });

        MainActivity.waitBar.setVisibility(View.INVISIBLE);

        name = getIntent().getStringExtra("name");
        checkApi = true;
        secondThread.start();

        new Handler().postDelayed(() -> {
            TextView pokemonNameView = findViewById(R.id.pokemonName);
            pokemonNameView.setText(name);

            ImageView pokemonImage = findViewById(R.id.pokemonImage);
            Picasso.get().load(currentPokemon.getImageURL()).into(pokemonImage);

            ListView movesList = findViewById(R.id.movesList);
            ArrayAdapter adapter = new ArrayAdapter<>(PokemonActivity.this, android.R.layout.simple_list_item_1, currentPokemon.getMoves());
            movesList.setAdapter(adapter);
            runOnUiThread(() -> adapter.notifyDataSetChanged());

        }, 3000);


    }

    //TODO: Check API to get all information concerning this pokemon and put items slowly on the activity
    //TODO: Check if there are images from the API
}