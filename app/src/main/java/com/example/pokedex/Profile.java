package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pokedex.roomDB.AppDatabase;
import com.example.pokedex.roomDB.PokemonDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    ArrayList<PokemonDB> pokemonList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navigationSetup();
        ArrayList<String> pokemonStringList = new ArrayList<>();

        pokemonList = loadCaughtPokemon();

        for (PokemonDB pokemon : pokemonList) {
            pokemonStringList.add(pokemon.pokemonCaught);
        }

        ListView listView = findViewById(R.id.pokemonList);
        adapter = new ArrayAdapter<>(Profile.this, android.R.layout.simple_list_item_1, pokemonStringList);
        listView.setAdapter(adapter);
        runOnUiThread(() -> adapter.notifyDataSetChanged());
    }

    public ArrayList<PokemonDB> loadCaughtPokemon(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name")
                .allowMainThreadQueries() //not advised
                .build();
        ArrayList<PokemonDB> temp = (ArrayList<PokemonDB>) db.pokemonDao().getAll();
        System.out.println(temp);
        return (ArrayList<PokemonDB>) db.pokemonDao().getAll();
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