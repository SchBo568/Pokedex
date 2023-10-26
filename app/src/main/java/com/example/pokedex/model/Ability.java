package com.example.pokedex.model;

import java.io.Serializable;

public class Ability implements Serializable {
    private String name, description, shortEffect, effect;

    public Ability(String name) {
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
