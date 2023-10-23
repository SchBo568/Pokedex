package com.example.pokedex.model;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;

public class Pokemon implements Serializable {
    private String name;
    private ArrayList<String> held_items, moves, stats;
    private ArrayList<Ability> abilities;
    private ArrayList<Type> types;
    private int weight, height;
    private String imageURL;

    public Pokemon(String name) {
        this.name = Character.toTitleCase(name.charAt(0)) + name.substring(1);
        //this.types = types;
    }

    public Pokemon(String name, ArrayList<Ability> abilities/*, ArrayList<String> held_items, ArrayList<String> moves, ArrayList<String> stats, ArrayList<Type> types*/, int weight, int height, String url) {
        this.name = name;
        this.imageURL = url;
        this.abilities = abilities;
        /*this.held_items = held_items;
        this.moves = moves;
        this.stats = stats;
        this.types = types;*/
        this.weight = weight;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getHeld_items() {
        return held_items;
    }

    public ArrayList<String> getMoves() {
        return moves;
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
}
