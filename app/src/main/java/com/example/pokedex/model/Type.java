package com.example.pokedex.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Type {
    private ObjectMapper objectMapper = new ObjectMapper();
    private int id;
    private String name;

    private String move_damage_class;
    //private Type[] double_damage_from, double_damage_to, half_damage_from, half_damage_to, no_damage_from_no_damage_to;

    public Type(String name) {
        this.name = name;

        try{
            URL url = new URL("https://pokeapi.co/api/v2/type/" + name);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            System.out.println(jsonNode);

            String pMoveClass = jsonNode.get("move_damage_class").get("name").asText();
            int pId = jsonNode.get("id").asInt();

            this.id = pId;
            this.move_damage_class = pMoveClass;
        }
        catch(Exception e){
            System.out.println("You fucked up");
        }

    }

    /*public Type[] convert(){

    }*/

    public String getName() {
        return name;
    }

}
