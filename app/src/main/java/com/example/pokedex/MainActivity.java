package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.pokedex.model.Pokemon;
import com.example.pokedex.network.Api;
import com.example.pokedex.roomDB.AppDatabase;
import com.example.pokedex.roomDB.PokemonDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ListView listView;
    public ProgressBar waitBar;
    private Api api = new Api();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> pokemonList = new ArrayList<>();

    private Spinner generationSpinner;
    private Pokemon currentPokemon;
    private String currentPokemonName;

    private ArrayList<PokemonDB> caughtPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api.secondThread.start();
        navigationSetup();

        waitBar = findViewById(R.id.waitBar);

        generationSpinner = findViewById(R.id.generationSpinner);
        api.loadPokemonList(generationSpinner.getSelectedItem().toString());
        listView = findViewById(R.id.pokemonList);

        loadPokemonList();
        selectPokemon();
    }

    /*
     * General information about load methods
     * Load methods will loop inside themselves with a 500ms delay in ordr to check the api
     * class for their corresponding finish variable
     * 
     */
    public void loadPokemonList(){
        new Handler().postDelayed(() -> {
            if(api.finishLoadingPokemonList){
                pokemonList = api.getPokemonNames();
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, pokemonList);
                listView.setAdapter(adapter);
                runOnUiThread(() -> adapter.notifyDataSetChanged());
                generationSpinner.setOnItemSelectedListener(this);
                handleSearchForPokemon();
                waitBar.setVisibility(View.INVISIBLE);
            }
            else{
                Log.d("Waiting", "loadPokemonList: ");
                loadPokemonList();
            }
        }, 500);
    }

    /*
     * The navigationSetup method can be found in every frontend class since it essentially does the same
     * on every page.
     * Yes I know this could certainly have been done otherwise
     */
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

    public void navigateToPokemonDetails() {
        new Handler().postDelayed(() -> {
            if (currentPokemon == null) {
                navigateToPokemonDetails();
            } else {
                /*
                    We fill the intent with all the pokemon information, so that the details screen
                    does not have to do any work and just displays all the information
                */
                Intent intent = new Intent(getApplicationContext(), PokemonActivity.class);
                intent.putExtra("pokemon", currentPokemon);
                intent.putExtra("name", currentPokemon.getName());
                startActivity(intent);

                api.finishLoadingPokemonDetails = false;
            }
        }, 500);
    }

    /*
     * This method defines the onclick event for when a user taps on one of the pokemon
     */
    public void selectPokemon() {
        ListView pokemonListView = findViewById(R.id.pokemonList);

        pokemonListView.setOnItemClickListener((adapterView, view, i, l) -> {
            String currentPokemoName = pokemonListView.getItemAtPosition(i).toString();
            api.loadPokemonDetails(currentPokemoName);
            if (waitBar != null) waitBar.setVisibility(View.VISIBLE);
            System.out.println("before second while");
            while(!api.finishLoadingPokemonDetails){}
            System.out.println("after second while");
            if (waitBar != null) waitBar.setVisibility(View.INVISIBLE);
            currentPokemon = api.getCurrentPokemon();
            navigateToPokemonDetails();

        });

    }

    public void handleSearchForPokemon() {
        EditText searchTextField = findViewById(R.id.searchText);
        searchTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pokemonList = api.searchName(charSequence.toString());
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, pokemonList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }

    
    /*
     * This method defines when an item from the generation spinner is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (waitBar != null) waitBar.setVisibility(View.VISIBLE);

        String generation = adapterView.getItemAtPosition(i).toString();
        api.loadPokemonList(generation);

        new Handler().postDelayed(() -> {
            pokemonList = api.getPokemonNames();
            adapter.addAll(pokemonList);
            handleSearchForPokemon();
            if (waitBar != null)
                waitBar.setVisibility(View.INVISIBLE);
        }, 2000);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
