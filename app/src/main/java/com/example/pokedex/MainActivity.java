package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.pokedex.network.Api;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ListView listView;
    private Api api = new Api();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> pokemonList = new ArrayList<>();

    private Spinner generationSpinner;

    private boolean checkApi = true;

    //This needs to run under a second thread to avoid the network on main error
    private final Thread secondThread = new Thread(() -> {
        while(true){
            if(checkApi){
                String generation = generationSpinner.getSelectedItem().toString();
                System.out.println(generation);
                pokemonList = api.loadPokemons(generation);
                checkApi = false;
            }
        }
    });

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
                listView.setAdapter(adapter);
                runOnUiThread(() -> adapter.notifyDataSetChanged());
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

        //Handler makes the program wait for 2 seconds until filling list with api values
        new Handler().postDelayed(() -> {
            listView = findViewById(R.id.pokemonList);
            pokemonList = api.getPokemonNames();
            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, pokemonList);
            listView.setAdapter(adapter);
            runOnUiThread(() -> adapter.notifyDataSetChanged());
            generationSpinner.setOnItemSelectedListener(this);
            //secondThread.suspend();
        }, 3000);
        handleSearchForPokemon();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        checkApi = true;

        new Handler().postDelayed(() -> {
            pokemonList = api.getPokemonNames();
            System.out.println(pokemonList.size());
            adapter.addAll(pokemonList);
            handleSearchForPokemon();
        }, 5000);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
