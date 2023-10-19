package com.example.pokedex.model;

import java.sql.Types;
import java.util.ArrayList;

public class Pokemon {
    private String name;
    private ArrayList<String> held_items, moves, stats;
    private ArrayList<Ability> abilities;
    private ArrayList<Type> types;
    private int weight, height;

    public Pokemon(String name) {
        this.name = Character.toTitleCase(name.charAt(0)) + name.substring(1);
        //this.types = types;
    }

    public Pokemon(String name, ArrayList<Ability> abilities, ArrayList<String> held_items, ArrayList<String> moves, ArrayList<String> stats, ArrayList<Type> types, int weight, int height) {

        this.abilities = abilities;
        this.held_items = held_items;
        this.moves = moves;
        this.stats = stats;
        this.types = types;
        this.weight = weight;
        this.height = height;
    }

    @Override
    public String toString() {
        return name;
    }
}
