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
    private ObjectMapper objectMapper = new ObjectMapper();
    private ArrayList<String> pokemonList = new ArrayList<>();
    private Map<String, Integer> lookup = new HashMap<>();
    private String generation, currentPokemonName, randomPokemonName;
    private Pokemon currentPokemon, randomPokemon;

    public boolean finishLoadingPokemonList,finishLoadingPokemonDetails, finishLoadingRandomPokemon = false;

    private boolean checkApi, loadCurrentPokemon, loadPokemonList, loadRandomPokemon = false;

    /*
        A seperate thread is needed to conduct any work over the network.
        How I approached this is to make public methods that set certain flags
        inside this class and the thread waits until certain flags are set to true
        before doing anything. This moves most of the logic to this class instead of the
        "frontend" classes.

        There are two different boolean variables that are very important in this class.
        You have the load variables, which tell this class to execute certain methods
        And you have the finished variables, which tell "frontend" class that a method has finished
        so that they can wait until everything is fully loaded.
    */

    public final Thread secondThread = new Thread(() -> {
        while (true) {
            if (checkApi) {
                checkApi = false;
                if (loadPokemonList) loadPokemons(); loadPokemonList = false; checkApi = false;
                if (loadRandomPokemon) getRandomPokemon(); checkApi = false;
                if (loadCurrentPokemon){
                    currentPokemon = new Pokemon(currentPokemonName);
                    System.out.println("before while");
                    while(currentPokemon == null) {/*wait*/}
                    System.out.println("after while");
                    checkApi = false;
                    finishLoadingPokemonDetails = true;
                }
            }
        }
    });

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

    public ArrayList<String> getPokemonNames() {
        return pokemonList;
    }

    public Pokemon getCurrentPokemon() {
        return currentPokemon;
    }

    public Pokemon getRandomPokemonObject(){
        return randomPokemon;
    }

    public void getRandomPokemonPublic(){
        loadRandomPokemon = true;
        checkApi = true;
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

    /*
        This method generates a random number and goes to search for a pokemon with this number
        This pokemon will be shown at a specific screen in order to be caught and send to the internal database
    */
    private void getRandomPokemon() {
        Random random = new Random();
        int randomNumber = random.nextInt(1016 - 1 + 1) + 1;

        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + randomNumber);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            Pokemon pokemon = new Pokemon(jsonNode.get("name").asText());
            randomPokemon = pokemon;
            finishLoadingRandomPokemon = true;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
        This method has the sole purpose to check the selected generation and then
        generate a list with all pokemon from this generation.
        For the list we only take the names of the pokemon and only later on
        when clicking on one of the pokemon, the whole pokemon details get loaded
    */
    private void loadPokemons() {
        try {
            int generationNumber = 1;
            if (!generation.equals("All generations")) {
                String[] split = generation.split(" ");
                generationNumber = lookup.get(split[0].toLowerCase());
            }

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
