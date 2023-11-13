package com.example.pokedex.roomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PokemonDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PokemonDao pokemonDao();
}
