package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pokedex.helperClasses.CardAdapter;
import com.example.pokedex.model.Ability;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.network.Api;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PokemonActivity extends AppCompatActivity {

    private Api api = new Api();
    private ProgressBar waitBar;
    private Pokemon currentPokemon;
    private String name = "";
    private boolean isLoaded = false;
    private HashMap<String, Integer> typesImages = new HashMap<String, Integer>();

    //This small hashmap makes filling in the images easier
    private void fillHashMap(){
        typesImages.put("normal", R.drawable.normal);
        typesImages.put("fire", R.drawable.fire);
        typesImages.put("water", R.drawable.water);
        typesImages.put("grass", R.drawable.grass);
        typesImages.put("flying", R.drawable.flying);
        typesImages.put("fighting", R.drawable.fighting);
        typesImages.put("poison", R.drawable.poison);
        typesImages.put("electric", R.drawable.electric);
        typesImages.put("ground", R.drawable.ground);
        typesImages.put("rock", R.drawable.rock);
        typesImages.put("psychic", R.drawable.psychic);
        typesImages.put("ice", R.drawable.ice);
        typesImages.put("bug", R.drawable.bug);
        typesImages.put("ghost", R.drawable.ghost);
        typesImages.put("steel", R.drawable.steel);
        typesImages.put("dragon", R.drawable.dragon);
        typesImages.put("dark", R.drawable.dark);
        typesImages.put("fairy", R.drawable.fairy);
        typesImages.put("none", R.drawable.none);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        waitBar = findViewById(R.id.waitBar2);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {this.finish();});

        fillHashMap();
        if(waitBar != null) waitBar.setVisibility(View.INVISIBLE);

        name = getIntent().getStringExtra("name");
        currentPokemon = (Pokemon) getIntent().getSerializableExtra("pokemon");

        fillInformations();

    }

    public void fillInformations() {
        TextView pokemonNameView = findViewById(R.id.pokemonName);
        pokemonNameView.setText(name);

        ImageView pokemonImage = findViewById(R.id.pokemonImage);
        Picasso.get().load(currentPokemon.getImageURL()).into(pokemonImage);

        String type1 = currentPokemon.getTypes().get(0).getName();
        String type2 = currentPokemon.getTypes().get(1).getName();

        ArrayList<Ability> abilityList = currentPokemon.getAbilities();

        String ability1 = abilityList.get(0).getName();
        String ability2 = abilityList.get(1).getName();

        Button abilityButton1 = findViewById(R.id.ability1);
        Button abilityButton2 = findViewById(R.id.ability2);

        abilityButton1.setText(ability1);
        abilityButton2.setText(ability2);

        ImageView iType1 = findViewById(R.id.type1);
        iType1.setImageResource(typesImages.get(type1));

        ImageView iType2 = findViewById(R.id.type2);
        iType2.setImageResource(typesImages.get(type2));

        TextView weightTW = findViewById(R.id.weight);
        String weight = "Weight: " + String.valueOf(currentPokemon.getWeight()) + "g";
        weightTW.setText(weight);

        TextView heightTW = findViewById(R.id.height);
        String height = "Height: "+ String.valueOf(currentPokemon.getHeight()) + "cm";
        heightTW.setText(height);

        ArrayList<String> moves = currentPokemon.getSMoves();
        RecyclerView rw = findViewById(R.id.recylcerView1);
        CardAdapter ca = new CardAdapter(moves);
        rw.setAdapter(ca);
        rw.setLayoutManager(new LinearLayoutManager(this));
    }

    public void navigationSetup() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent intent = new Intent(getApplicationContext(), CatchRandomPokemon.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.search) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.profile) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
            return true;
        });
    }
}