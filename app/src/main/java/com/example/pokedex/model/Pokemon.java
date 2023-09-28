package com.example.pokedex.model;

public class Pokemon {
    private String name;

    public Pokemon(String name) {
        this.name = Character.toTitleCase(name.charAt(0)) + name.substring(1);
    }

    @Override
    public String toString() {
        return name;
    }
}
