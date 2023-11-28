package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokedex.model.Pokemon;
import com.example.pokedex.network.Api;
import com.example.pokedex.roomDB.AppDatabase;
import com.example.pokedex.roomDB.PokemonDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CatchRandomPokemon extends AppCompatActivity {

    private Api api = new Api();
    private Pokemon currentPokemon;
    private ArrayList<PokemonDB> caughtPokemon;
    private boolean pokemonShown, addPokemonToDb, loadCaughtPokemon, checkApi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_random_pokemon);
        api.secondThread.start();
        navigationSetup();
        loadRandomPokemon();

        System.out.println("outside condition");
        while(!pokemonShown) {
            System.out.println("inside condition");
        }

        if (currentPokemon != null) {
            pokemonShown = true;
            ImageView iw = findViewById(R.id.randomPokemonImageVie);
            TextView tv = findViewById(R.id.pokemonNameLabel);
            tv.setText(currentPokemon.getName());
            Picasso.get().load(currentPokemon.getImageURL()).resize(200, 200).into(iw);

            Button button = findViewById(R.id.catchButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addPokemonToDb = true;
                }
            });
        }
    }

    public void loadRandomPokemon(){
        api.getRandomPokemonPublic();
        //new Handler().postDelayed(() -> {
        while(true){
            if(api.finishLoadingRandomPokemon){
                currentPokemon = api.getRandomPokemonObject();
                pokemonShown = true;
                api.finishLoadingRandomPokemon = false;
                break;
            }
        }

        //}, 500);
    }



    public void navigationSetup() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent intent = new Intent(getApplicationContext(), CatchRandomPokemon.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.search) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.profile) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
            return true;
        });
    }
}