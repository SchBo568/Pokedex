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
    public static ProgressBar waitBar;
    private Api api = new Api();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> pokemonList = new ArrayList<>();

    private Spinner generationSpinner;
    private Pokemon currentPokemon;
    private String currentPokemonName;

    private boolean checkApi = true;
    private boolean pokemonDetails = false;
    private boolean loadCurrentPokemon = false;
    private boolean loadCaughtPokemon = false;
    private ArrayList<PokemonDB> caughtPokemon;

    //This needs to run under a second thread to avoid the network on main error
    private final Thread secondThread = new Thread(() -> {
        while(true){
            if(checkApi){
                pokemonList = api.loadPokemons(generationSpinner.getSelectedItem().toString());
                checkApi = false;
                if(loadCurrentPokemon){
                    currentPokemon = new Pokemon(currentPokemonName);
                }

                if(loadCaughtPokemon){
                    Log.d("ugabuga", "test: ");
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "database-name").build();
                    caughtPokemon = (ArrayList<PokemonDB>) db.pokemonDao().getAll();
                    System.out.println(caughtPokemon);
                }
            }
        }
    });

    public void selectPokemon(){
        ListView listView1 = findViewById(R.id.pokemonList);
        listView1.setOnItemClickListener((adapterView, view, i, l) -> {
            String s = listView1.getItemAtPosition(i).toString();
            currentPokemonName = s;
            loadCurrentPokemon = true;
            checkApi = true;
            if(waitBar != null)
                waitBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getApplicationContext(), PokemonActivity.class);
                intent.putExtra("pokemon", currentPokemon);
                intent.putExtra("name", currentPokemonName);
                pokemonDetails = true;
                startActivity(intent);
            }, 5000);

        });

    }

    public void handleSearchForPokemon(){
        EditText searchTextField = findViewById(R.id.searchText);
        searchTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String searchText = charSequence.toString();
                pokemonList = api.searchName(searchText);
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, pokemonList);
                if(adapter != null && pokemonList != null){
                    listView.setAdapter(adapter);
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //do nothing
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generationSpinner = findViewById(R.id.generationSpinner);
        secondThread.start();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId =  item.getItemId();

                if(menuItemId == R.id.home){
                    Intent intent = new Intent(getApplicationContext(), CatchRandomPokemon.class);
                    startActivity(intent);
                    return true;
                }
                else if(menuItemId == R.id.search){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(menuItemId == R.id.profile){
                    loadCaughtPokemon = true;
                    checkApi = true;
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(getApplicationContext(), Profile.class);

                        intent.putExtra("caughtPokemon", caughtPokemon);
                        startActivity(intent);

                    }, 5000);
                    return true;
                }
                return true;
            }
        });


        //Handler makes the program wait for 2 seconds until filling list with api values
        new Handler().postDelayed(() -> {
            listView = findViewById(R.id.pokemonList);
            pokemonList = api.getPokemonNames();
            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, pokemonList);
            listView.setAdapter(adapter);
            runOnUiThread(() -> adapter.notifyDataSetChanged());
            generationSpinner.setOnItemSelectedListener(this);
        }, 3000);
        handleSearchForPokemon();
        selectPokemon();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        checkApi = true;
        if(waitBar != null)
            waitBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            pokemonList = api.getPokemonNames();
            adapter.addAll(pokemonList);
            handleSearchForPokemon();
            if(waitBar != null)
                waitBar.setVisibility(View.INVISIBLE);
        }, 3000);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
