package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.pokedex.roomDB.PokemonDB;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ArrayList<PokemonDB> temp = (ArrayList<PokemonDB>) getIntent().getSerializableExtra("caughtPokemon");
        for (int i = 0; i < temp.size(); i++) {
            System.out.println(temp.get(i).pokemonCaught);
        }

    }
}