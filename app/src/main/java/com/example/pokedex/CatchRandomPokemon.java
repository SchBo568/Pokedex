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

    private Api api;
    private Pokemon currentPokemon;
    private ArrayList<PokemonDB> caughtPokemon;
    private boolean pokemonShown, addPokemonToDb, loadCaughtPokemon, checkApi = false;
    public final Thread secondThread = new Thread(() -> {
        api = new Api();
        currentPokemon = api.getRandomPokemon();
        while(!addPokemonToDb){
            if(addPokemonToDb){
                System.out.println("ungabunga");
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name").build();
                db.pokemonDao().insert(new PokemonDB(currentPokemon.getName()));
                //break;
            }
            if(loadCaughtPokemon){
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name").build();
                caughtPokemon = (ArrayList<PokemonDB>) db.pokemonDao().getAll();
            }
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_random_pokemon);
        secondThread.start();

        BottomNavigationView bvn = findViewById(R.id.bottom_navigation);
        bvn.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId =  item.getItemId();

                if(menuItemId == R.id.home){
                    Intent intent = new Intent(getApplicationContext(), CatchRandomPokemon.class);
                    startActivity(intent);
                    return true;
                }
                else if(menuItemId == R.id.search){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(menuItemId == R.id.profile){
                    return true;
                }
                return true;
            }
        });

        while(!pokemonShown){
            if(currentPokemon != null){
                pokemonShown = true;

                ImageView iw = findViewById(R.id.randomPokemonImageVie);
                TextView tv = findViewById(R.id.pokemonNameLabel);
                tv.setText(currentPokemon.getName());
                Picasso.get().load(currentPokemon.getImageURL()).resize(200,200).into(iw);

                Button button = findViewById(R.id.catchButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addPokemonToDb = true;
                    }
                });
            }
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId =  item.getItemId();

                if(menuItemId == R.id.home){
                    Intent intent = new Intent(getApplicationContext(), CatchRandomPokemon.class);
                    startActivity(intent);
                    return true;
                }
                else if(menuItemId == R.id.search){

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(menuItemId == R.id.profile){
                    loadCaughtPokemon = true;
                    checkApi = true;
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(getApplicationContext(), Profile.class);

                        intent.putExtra("caughtPokemon", caughtPokemon);
                        startActivity(intent);

                    }, 3000);
                    return true;
                }
                return true;
            }
        });}
}