package com.example.pokedex.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.example.pokedex.model.Pokemon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Api {
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayList<Pokemon> pokemonList = new ArrayList<>();

    private void loadPokemons(String generation){
        try {
            int generationNumber = 1;
            if(!generation.equals("All generations")){
                String[] split = generation.split(" ");
                generationNumber = Integer.parseInt(split[1]) + 1;
            }

            //Pokedex:
            //TODO: Change the list from a generations list to a gameslist
            URL url = new URL("https://pokeapi.co/api/v2/pokedex/" + generationNumber);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode pokemons = jsonNode.get("pokemon_entries");

            for(JsonNode pokemon : pokemons) {
                String pokemonName = pokemon.get("pokemon_species").get("name").asText();
                pokemonList.add(new Pokemon(pokemonName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Api(String generation) {
        loadPokemons(generation);
    }

    public ArrayList<String> getPokemonNames() {
        ArrayList<String> result = new ArrayList<>();
        for (Pokemon pokemon: pokemonList) {
            result.add(pokemon.toString());
        }
        return result;
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
