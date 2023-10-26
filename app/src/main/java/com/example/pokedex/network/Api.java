package com.example.pokedex.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.pokedex.model.Ability;
import com.example.pokedex.model.Pokemon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Api {
    //TODO: Think about renaming file to something else

    private ObjectMapper objectMapper = new ObjectMapper();
    private ArrayList<String> pokemonList = new ArrayList<>();

    private Map<String, Integer> lookup = new HashMap<>();

    public Api() {
        lookup.put("kanto", 1);
        lookup.put("johto", 2);
        lookup.put("hoenn", 3);
        lookup.put("sinnoh", 4);
        lookup.put("unova", 5);
        lookup.put("kalos", 6);
        lookup.put("alola", 7);
        lookup.put("galar", 8);
        lookup.put("paldea", 9);
    }

    public ArrayList<String> loadPokemons(String generation){
        try {
            int generationNumber = 1;
            if(!generation.equals("All generations")){
                String[] split = generation.split(" ");
                generationNumber = lookup.get(split[0].toLowerCase());
            }

            //Pokedex:
            URL url = new URL("https://pokeapi.co/api/v2/generation/" + generationNumber);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode pokemons = jsonNode.get("pokemon_species");
            pokemonList.clear();
            for(JsonNode pokemon : pokemons) {
                String pokemonName = pokemon.get("name").asText();
                pokemonList.add(pokemonName);
            }
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemonList;
    }



    public ArrayList<String> getPokemonNames() {
        return pokemonList;
    }

    public ArrayList<String> searchName(String input){
        ArrayList<String> result = new ArrayList<>();
        for (String name: getPokemonNames()) {
            if(name.contains(input)){
                result.add(name);
            }
        }
        return result;
    }
}
