package com.example.pokedex.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Move {
    //If accuracy is null, then it cant miss, convert to 100
    private String name, damage_class, effect, short_effect, ailment, target;
    private int id, pp, priority, power,  accuracy, ailment_chance, crit_rate, drain, flinch_chance, healing, max_hits, max_turns, min_hits, min_turns, stat_chance;
    private Type type;
    private HashMap<Integer, Stat> stat_changes;

    public Move(String name, String damage_class, String effect, String short_effect, String ailment, String target, int id, int pp, int priority, int power, int accuracy, int ailment_chance, int crit_rate, int drain, int flinch_chance, int healing, int max_hits, int max_turns, int min_hits, int min_turns, int stat_chance, Type type, HashMap<Integer, Stat> stat_changes) {
        this.name = name;
        this.damage_class = damage_class;
        this.effect = effect;
        this.short_effect = short_effect;
        this.ailment = ailment;
        this.target = target;
        this.id = id;
        this.pp = pp;
        this.priority = priority;
        this.power = power;
        this.accuracy = accuracy;
        this.ailment_chance = ailment_chance;
        this.crit_rate = crit_rate;
        this.drain = drain;
        this.flinch_chance = flinch_chance;
        this.healing = healing;
        this.max_hits = max_hits;
        this.max_turns = max_turns;
        this.min_hits = min_hits;
        this.min_turns = min_turns;
        this.stat_chance = stat_chance;
        this.type = type;
        this.stat_changes = stat_changes;
    }
}
