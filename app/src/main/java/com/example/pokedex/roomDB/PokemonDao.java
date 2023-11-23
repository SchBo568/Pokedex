package com.example.pokedex.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pokedex.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PokemonDao {
    @Query("SELECT * FROM pokemondb")
    List<PokemonDB> getAll();

    @Insert
    void insert(PokemonDB pokemon);

    @Delete
    void delete(PokemonDB pokemon);
}
