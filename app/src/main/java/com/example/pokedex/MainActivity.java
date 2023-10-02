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
    private Api api;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> pokemonList = new ArrayList<>();

    private boolean checkApi = true;

    private Thread secondThread = new Thread(() -> {
        while(true){
            if(checkApi){
                Spinner tempSpinner = findViewById(R.id.generationSpinner);
                String generation = tempSpinner.getSelectedItem().toString();
                api = new Api(generation);
                checkApi = false;
            }
        }
    });

    public void handleSearchForPokemon(){
        EditText searchTf = findViewById(R.id.searchText);
        searchTf.addTextChangedListener(new TextWatcher() {
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
        secondThread.start();



        //Handler makes the programm wait for 2 seconds until filling list with api values
        new Handler().postDelayed(() -> {
            listView = findViewById(R.id.pokemonList);
            pokemonList = api.getPokemonNames();
            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, pokemonList);
            listView.setAdapter(adapter);
            runOnUiThread(() -> adapter.notifyDataSetChanged());

            Spinner spinner = (Spinner) findViewById(R.id.generationSpinner);
            spinner.setOnItemSelectedListener(this);
            //secondThread.suspend();
        }, 3000);
        handleSearchForPokemon();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        checkApi = true;

        new Handler().postDelayed(() -> {
            pokemonList = api.getPokemonNames();
            adapter.clear();
            adapter.addAll(pokemonList);
            Log.d("test", pokemonList.get(1));
            handleSearchForPokemon();
        }, 3000);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
