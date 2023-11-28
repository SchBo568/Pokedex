package com.example.pokedex.network;

import android.util.Log;

import androidx.room.Room;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.pokedex.model.Ability;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.roomDB.AppDatabase;
import com.example.pokedex.roomDB.PokemonDB;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Api {
    // TODO: Think about renaming file to something else

    private ObjectMapper objectMapper = new ObjectMapper();
    private ArrayList<String> pokemonList = new ArrayList<>();
    private Map<String, Integer> lookup = new HashMap<>();
    private String generation, currentPokemonName, randomPokemonName;
    private Pokemon currentPokemon, randomPokemon;

    public boolean finishLoadingPokemonList,finishLoadingPokemonDetails, finishLoadingRandomPokemon = false;

    private boolean checkApi, loadCurrentPokemon, loadPokemonList, loadRandomPokemon = false;

    public final Thread secondThread = new Thread(() -> {
        while (true) {
            if (checkApi) {
                checkApi = false;
                if (loadPokemonList) loadPokemons(); loadPokemonList = false; checkApi = false;
                if (loadCurrentPokemon){
                    System.out.println("before creating new pokemon");
                    currentPokemon = new Pokemon(currentPokemonName);
                    System.out.println("after creating pokemon");
                    while(currentPokemon == null) {

                    }
                    System.out.println("after while");
                    checkApi = false;
                    finishLoadingPokemonDetails = true;
                }
                if (loadRandomPokemon) getRandomPokemon(); checkApi = false;
            }
        }
    });

    public Pokemon getCurrentPokemon() {
        return currentPokemon;
    }

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

    public void getRandomPokemonPublic(){
        loadRandomPokemon = true;
        checkApi = true;
        System.out.println("setting booleans to true");
    }

    public Pokemon getRandomPokemonObject(){
        return randomPokemon;
    }



    private void getRandomPokemon() {
        Random random = new Random();
        int randomNumber = random.nextInt(1016 - 1 + 1) + 1;

        try {
            System.out.println("fetching random pokemon");
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + randomNumber);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            Pokemon pokemon = new Pokemon(jsonNode.get("name").asText());
            randomPokemon = pokemon;
            finishLoadingRandomPokemon = true;
            System.out.println("finished fertching pokemon");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadPokemonList(String generation){
        loadPokemonList = true;
        this.generation = generation;
        checkApi = true;
    }

    public void loadPokemonDetails(String pokemonName){
        this.currentPokemon = null;
        loadCurrentPokemon = true;
        this.currentPokemonName = pokemonName;
        checkApi = true;
    }

    private void loadPokemons() {
        try {
            int generationNumber = 1;
            if (!generation.equals("All generations")) {
                String[] split = generation.split(" ");
                generationNumber = lookup.get(split[0].toLowerCase());
            }

            // Pokedex:
            URL url = new URL("https://pokeapi.co/api/v2/generation/" + generationNumber);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode pokemons = jsonNode.get("pokemon_species");
            pokemonList.clear();
            for (JsonNode pokemon : pokemons) {
                String pokemonName = pokemon.get("name").asText();
                pokemonList.add(pokemonName);
            }


            httpURLConnection.disconnect();

            finishLoadingPokemonList = true;
        } catch (Exception e) {
            e.printStackTrace();
        };
    }

    public ArrayList<String> getPokemonNames() {
        return pokemonList;
    }

    public ArrayList<String> searchName(String input) {
        ArrayList<String> result = new ArrayList<>();
        for (String name : getPokemonNames()) {
            if (name.contains(input)) {
                result.add(name);
            }
        }
        return result;
    }
}
