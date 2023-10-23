package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokedex.model.Pokemon;
import com.example.pokedex.network.Api;
import com.squareup.picasso.Picasso;

public class PokemonActivity extends AppCompatActivity {

    private Api api = new Api();
    private boolean checkApi = false;
    private Pokemon currentPokemon;
    private String name = "";



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

        currentPokemon  = (Pokemon) getIntent().getSerializableExtra("pokemon");
        System.out.println(currentPokemon);
        TextView pokemonNameView = findViewById(R.id.pokemonName);
        pokemonNameView.setText(name);

        ImageView pokemonImage = findViewById(R.id.pokemonImage);
        Picasso.get().load(currentPokemon.getImageURL()).into(pokemonImage);

    }

    //TODO: Check API to get all information concerning this pokemon and put items slowly on the activity
    //TODO: Check if there are images from the API
}