package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pokedex.helperClasses.CardAdapter;
import com.example.pokedex.model.Move;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.network.Api;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PokemonActivity extends AppCompatActivity {

    private Api api = new Api();
    private boolean checkApi = false;
    private Pokemon currentPokemon;
    private String name = "";
    private boolean isLoaded = false;

    private final Thread secondThread = new Thread(() -> {
        Thread.currentThread().setName("LOOOOOOOOOOK");
        while(true){
            if(checkApi){
                currentPokemon = new Pokemon(name);
                if(currentPokemon != null){
                    if(currentPokemon.getSMoves().size() == currentPokemon.amountOfMoves){
                        checkApi = false;
                        isLoaded = true;
                    }
                }
            }

        }
    }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.waitBar.setVisibility(View.INVISIBLE);
        setContentView(R.layout.activity_pokemon);

        name = getIntent().getStringExtra("name");
        secondThread.start();
        checkApi = true;

        //TODO: DONT DO THAT
        while(true){
            if(isLoaded){
                TextView pokemonNameView = findViewById(R.id.pokemonName);
                pokemonNameView.setText(name);

                ImageView pokemonImage = findViewById(R.id.pokemonImage);
                Picasso.get().load(currentPokemon.getImageURL()).into(pokemonImage);

                Button backButton = (Button) findViewById(R.id.backButton);
                backButton.setOnClickListener(view -> {
                    secondThread.interrupt();
                    this.finish();

                });

                String type1 = currentPokemon.getTypes().get(0).getName();
                String type2 = currentPokemon.getTypes().get(1).getName();

                Button bType1 = findViewById(R.id.type1);
                Button bType2 = findViewById(R.id.type2);

                bType1.setText(type1);
                bType2.setText(type2);

                TextView weightTW = findViewById(R.id.weight);
                weightTW.setText("Weight: " +currentPokemon.getWeight() + "g");

                TextView heightTW = findViewById(R.id.height);
                heightTW.setText("Height: "+ currentPokemon.getHeight() + "cm");

                /*ListView movesList = findViewById(R.id.movesList);
                ArrayAdapter adapter = new ArrayAdapter<>(PokemonActivity.this, android.R.layout.simple_list_item_1, currentPokemon.getMoves());
                movesList.setAdapter(adapter);
                runOnUiThread(() -> adapter.notifyDataSetChanged());*/

                ArrayList<String> moves = currentPokemon.getSMoves();
                RecyclerView rw = findViewById(R.id.recylcerView1);
                CardAdapter ca = new CardAdapter(moves);
                rw.setAdapter(ca);
                rw.setLayoutManager(new LinearLayoutManager(this));

                break;
            }
        }
    }

    //TODO: Check API to get all information concerning this pokemon and put items slowly on the activity
    //TODO: Check if there are images from the API
}