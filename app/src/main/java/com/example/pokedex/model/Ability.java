package com.example.pokedex.model;

public class Ability {
    private String name, description, shortEffect, effect;

    public Ability(String name, String description, String shortEffect, String effect) {
        this.name = name;
        this.description = description;
        this.shortEffect = shortEffect;
        this.effect = effect;
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
