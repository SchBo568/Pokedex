package com.example.pokedex.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class Ability implements Serializable {
    private String name, description, shortEffect, effect;
    private ObjectMapper objectMapper = new ObjectMapper();

    public Ability(String urlS) {
        try{
            URL url = new URL(urlS);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            this.name = jsonNode.get("name").asText();

            for(JsonNode text : jsonNode.get("flavor_text_entries")){
                this.description = text.get("flavor_text").asText();
                break;
            }

            for(JsonNode effect : jsonNode.get("effect_entries")){
                this.shortEffect = effect.get("short_effect").asText();
                this.effect = effect.get("effect").asText();
            }
        }
        catch(Exception e){
            System.out.println("lol");
        }

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getShortEffect() {
        return shortEffect;
    }

    public String getEffect() {
        return effect;
    }

    public String toString(){
        return name + " - " + description + " - " + shortEffect + " - " + effect;
    }
}
