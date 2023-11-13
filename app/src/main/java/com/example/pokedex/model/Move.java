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
import java.util.ArrayList;
import java.util.HashMap;

public class Move implements Serializable {
    //If accuracy is null, then it cant miss, convert to 100
    private String name, damage_class, effect, short_effect, ailment, target, type;
    private int id, pp, priority, power,  accuracy, ailment_chance, crit_rate, drain, flinch_chance, healing, max_hits, max_turns, min_hits, min_turns, stat_chance;
    private HashMap<Integer, String> stat_changes;

    private ObjectMapper objectMapper = new ObjectMapper();

    public Move(String name) {
        try{
            URL url = new URL("https://pokeapi.co/api/v2/move/" + name);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonString = bufferedReader.readLine();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            this.accuracy = jsonNode.get("accuracy").asInt();
            this.damage_class = jsonNode.get("damage_class").get("name").asText();
            JsonNode effectArray = jsonNode.get("effect_entries");
            for(JsonNode effect : effectArray){
                this.effect = effect.get("effect").asText();
                this.short_effect = effect.get("short_effect").asText();
            }

            this.id = jsonNode.get("id").asInt();
            this.name = name;
            JsonNode metaData = jsonNode.get("meta");
            this.ailment = metaData.get("ailment").get("name").asText();
            this.ailment_chance = metaData.get("ailment_chance").asInt();
            this.crit_rate = metaData.get("crit_rate").asInt();
            this.drain = metaData.get("drain").asInt();
            this.flinch_chance = metaData.get("flinch_chance").asInt();
            this.healing = metaData.get("healing").asInt();

            if(metaData.get("max_hits") == null){
                this.max_hits = 1;
                this.min_hits = 1;
            }
            else{
                this.max_hits = metaData.get("max_hits").asInt();
                this.min_hits = metaData.get("min_hits").asInt();
            }
            if(metaData.get("max_turns") == null){
                this.max_turns = 1;
                this.min_turns = 1;
            }
            else{
                this.max_turns = metaData.get("max_turns").asInt();
                this.min_turns = metaData.get("min_turns").asInt();
            }
            this.stat_chance = metaData.get("stat_chance").asInt();

            this.power = jsonNode.get("power").asInt();
            this.pp = jsonNode.get("pp").asInt();
            this.priority = jsonNode.get("priority").asInt();
            this.stat_changes = new HashMap<>();
            //TODO: Check stat changes
            this.target = jsonNode.get("target").get("name").asText();
            this.type = jsonNode.get("type").get("name").asText();
            httpURLConnection.disconnect();
        }
        catch (Exception e){
            Log.d("findMove", "Not found");
            this.name = "Not found";
        }
    }

    public String getName() {
        return name;
    }

    public String getDetails(){
        return name + ";" + "Type: " + type + " - PP: " + pp + " - Power: " + power + " - Accuracy: " + accuracy;
    }

    @Override
    public String toString() {
        return "Move{" +
                "name='" + name + '\'' +
                ", damage_class='" + damage_class + '\'' +
                ", effect='" + effect + '\'' +
                ", short_effect='" + short_effect + '\'' +
                ", ailment='" + ailment + '\'' +
                ", target='" + target + '\'' +
                ", id=" + id +
                ", pp=" + pp +
                ", priority=" + priority +
                ", power=" + power +
                ", accuracy=" + accuracy +
                ", ailment_chance=" + ailment_chance +
                ", crit_rate=" + crit_rate +
                ", drain=" + drain +
                ", flinch_chance=" + flinch_chance +
                ", healing=" + healing +
                ", max_hits=" + max_hits +
                ", max_turns=" + max_turns +
                ", min_hits=" + min_hits +
                ", min_turns=" + min_turns +
                ", stat_chance=" + stat_chance +
                ", type=" + type +
                ", stat_changes=" + stat_changes +
                '}';
    }
}
