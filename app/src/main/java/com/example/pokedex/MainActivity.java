package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.pokedex.network.Api;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    Api api;
    ArrayAdapter<String> adapter;
    ArrayList<String> temp = new ArrayList<>();
    Thread secondThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        secondThread = new Thread(() -> {
            api = new Api();
        });
        secondThread.start();

        //Handler makes the programm wait for 2 seconds until filling list with api values
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                lv = findViewById(R.id.pokemonList);
                temp = api.getPokemonNames();
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, temp);
                lv.setAdapter(adapter);
                runOnUiThread(() -> adapter.notifyDataSetChanged());
                //secondThread.suspend();
            }
        }, 2000);

        EditText searchTf = findViewById(R.id.searchText);
        searchTf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString();
                temp = api.searchName(searchText);
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, temp);
                lv.setAdapter(adapter);
                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //do nothing
            }
    });
    }

}
