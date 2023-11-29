package com.example.pokedex.model;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.sql.Types;
import java.util.ArrayList;

public class Pokemon implements Serializable {
    private String name;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ArrayList<String> held_items, stats;
    private ArrayList<Move> moves;
    private ArrayList<String> sMoves;
    private ArrayList<Ability> abilities;
    private ArrayList<Type> types;
    private int weight, height;
    private String imageURL;
    public int amountOfMoves;
    public int id;


    /*
        Whenever a new instance of a pokemon is created, the constructor searches for all
        the needed information for itself and fills it in.

        Small detail: Every pokemon has 2 types, but if the pokemon is a mono-type
        it just gets a second type called "none"
    */
    public Pokemon(String name) {
        this.name = name;/*name.substring(0,1).toUpperCase() + name.substring(1);*/
        try{
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + name);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            ArrayList<Ability> abilities = new ArrayList<>();
            for(JsonNode ability : jsonNode.get("abilities")){
                abilities.add(new Ability(ability.get("ability").get("url").asText()));
            }

            this.abilities = abilities;
            this.id = jsonNode.get("id").asInt();
            this.weight = jsonNode.get("weight").asInt();
            this.height = jsonNode.get("height").asInt();

            this.imageURL = jsonNode.get("sprites").get("front_default").asText();

            ArrayList<Type> types = new ArrayList<>();
            for(JsonNode type : jsonNode.get("types")){
                Type actualType = new Type(type.get("type").get("name").asText());
                types.add(actualType);
            }

            if(types.size() ==1){
                types.add(new Type("none"));
            }

            ArrayList<String> moves = new ArrayList<>();
            amountOfMoves = jsonNode.get("moves").size();
            for(JsonNode move : jsonNode.get("moves")){
                String tempMove = move.get("move").get("name").asText();
                tempMove = tempMove.replace("-", " ");
                tempMove = tempMove.substring(0,1).toUpperCase() + tempMove.substring(1);
                moves.add(tempMove);
            }
            this.sMoves = moves;
            this.types = types;
            httpURLConnection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getSMoves(){

        /*ArrayList<String> temp = new ArrayList<>();
        for(Move move: moves){
            Log.d("moves", move.getName());
            temp.add(move.getName());
        }
        return temp;*/

        return sMoves;
    }

    public ArrayList<String> getHeld_items() {
        return held_items;
    }


    public ArrayList<String> getStats() {
        return stats;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public String toString() {
        return name + " - " + imageURL + " - " + weight + " - " + height;
    }

    public String movesToString(){
        String result = "";
        for(String move : getSMoves()){
            result = result + move + "\n";
        }
        return result;
    }
}
