package com.example.pokedex.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.example.pokedex.model.Ability;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.model.Type;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Api {
    public static final int NATIONAL_DEX = 1;
    public static final int KANTO = 2;
    public static final int JOHTO = 3;
    public static final int HOENN = 4;
    public static final int SINNOH = 5;
    public static final int EXTENDED_SINNOH = 6;
    public static final int UPDATED_JOHTO = 7;
    public static final int UNOVA = 8;
    public static final int UPDATED_UNOVA = 9;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ArrayList<String> pokemonList = new ArrayList<>();

    public ArrayList<String> loadPokemons(String generation){
        ArrayList<String> result = new ArrayList<>();
        try {
            int generationNumber = 1;
            if(!generation.equals("All generations")){
                String[] split = generation.split(" ");
                generationNumber = Integer.parseInt(split[1]) + 1;
            }

            //Pokedex:
            //TODO: Change the list from a generations list to a regionlist
            URL url = new URL("https://pokeapi.co/api/v2/pokedex/" + generationNumber);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode pokemons = jsonNode.get("pokemon_entries");
            pokemonList.clear();
            for(JsonNode pokemon : pokemons) {
                String pokemonName = pokemon.get("pokemon_species").get("name").asText();
                pokemonList.add(pokemonName);
                //pokemonList.add(getPokemonDetails(pokemonName));
            }
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result = pokemonList;
        return result;
    }

    public Api() {
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

    //TODO: Check for null output if api did not respond
    //TODO: items, moves, stats, types (for item, stats and types i can use maps to not make seperate classes)
    //Notice 1: If a pokemon cannot be found, first search for its pokemon-species and then check if it does not exist
    //Notice 2: always make the search a minuscule
    public Pokemon getPokemonDetails(String name){
        Pokemon pokemon = null;
        try{
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + name);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            ArrayList<Ability> abilities = new ArrayList<>();
            for(JsonNode ability : jsonNode.get("abilities")){
                abilities.add(getPokemonAbility(ability.get("ability").get("url").asText()));
            }

            /*ArrayList<Type> types = new ArrayList<>();
            for(JsonNode type : jsonNode.get("types")){
                Type actualType = new Type(type.get("type").get("name").asText());
                types.add(actualType);
            }
            */
            pokemon = new Pokemon(jsonNode.get("name").asText());
            httpURLConnection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return pokemon;
    }

    public Ability getPokemonAbility(String urlS){
        Ability result = null;
        try{
            URL url = new URL(urlS);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            //System.out.println(jsonNode.asText());
            String name = String.valueOf(jsonNode.get("name"));
            String description = String.valueOf(jsonNode.get("flavor_text_entries").get(1).get("flavor_text"));
            String shortEffect = String.valueOf(jsonNode.get("effect_entries").get(1).get("short_effect"));
            String effect = String.valueOf(jsonNode.get("effect_entries").get(1).get("effect"));

            result = new Ability(name, description, shortEffect, effect);
            System.out.println(result);
            //JsonNode[] names = new JsonNode[]{jsonNode.get("names")};
            httpURLConnection.disconnect();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
