package com.example.pokedex.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PokemonDB {
    public PokemonDB(String pokemonCaught) {
        this.pokemonCaught = pokemonCaught;
    }

    @PrimaryKey(autoGenerate = true)
    public int catchId;

    @ColumnInfo(name = "pokemon_caught")
    public String pokemonCaught;
}
