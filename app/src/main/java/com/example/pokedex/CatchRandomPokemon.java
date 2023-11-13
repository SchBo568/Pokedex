package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.example.pokedex.model.Pokemon;
import com.example.pokedex.network.Api;
import com.example.pokedex.roomDB.AppDatabase;
import com.example.pokedex.roomDB.PokemonDB;

import java.util.List;

public class CatchRandomPokemon extends AppCompatActivity {

    private Api api;
    private final Thread secondThread = new Thread(() -> {
        api = new Api();

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_random_pokemon);

        //TODO: 1) Choose a random pokemon 2) Button to catch it and put to internal db 3) only 1 capture every 2 hours 4) reroll once per day
    }
}