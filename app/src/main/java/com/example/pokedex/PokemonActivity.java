package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pokedex.helperClasses.CardAdapter;
import com.example.pokedex.model.Move;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.network.Api;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PokemonActivity extends AppCompatActivity {

    private Api api = new Api();
    private boolean checkApi = false;
    private Pokemon currentPokemon;
    private String name = "";
    private boolean isLoaded = false;
    private HashMap<String, Integer> typesImages = new HashMap<String, Integer>();


    private void fillHM(){
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
        fillHM();
        MainActivity.waitBar.setVisibility(View.INVISIBLE);
        setContentView(R.layout.activity_pokemon);
        BottomNavigationView bvn = findViewById(R.id.bottom_navigation);

        bvn.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId =  item.getItemId();

                if(menuItemId == R.id.home){
                    return true;
                }
                else if(menuItemId == R.id.search){

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(menuItemId == R.id.profile){
                    return true;
                }
                return true;
            }
        });

        name = getIntent().getStringExtra("name");
        currentPokemon = (Pokemon) getIntent().getSerializableExtra("pokemon");


        TextView pokemonNameView = findViewById(R.id.pokemonName);
        pokemonNameView.setText(name);

        ImageView pokemonImage = findViewById(R.id.pokemonImage);
        Picasso.get().load(currentPokemon.getImageURL()).into(pokemonImage);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            this.finish();

        });

        String type1 = currentPokemon.getTypes().get(0).getName();
        String type2 = currentPokemon.getTypes().get(1).getName();

        ImageView iType1 = findViewById(R.id.type1);
        iType1.setImageResource(typesImages.get(type1));

        ImageView iType2 = findViewById(R.id.type2);
        iType2.setImageResource(typesImages.get(type2));

        TextView weightTW = findViewById(R.id.weight);
        String weight = "Weight: " + /*String.valueOf(currentPokemon.getWeight()) +*/ "g";
        weightTW.setText(weight);

        TextView heightTW = findViewById(R.id.height);
        String height = "Height: "+ String.valueOf(currentPokemon.getHeight()) + "cm";
        heightTW.setText(height);

        /*ListView movesList = findViewById(R.id.movesList);
        ArrayAdapter adapter = new ArrayAdapter<>(PokemonActivity.this, android.R.layout.simple_list_item_1, currentPokemon.getMoves());
        movesList.setAdapter(adapter);
        runOnUiThread(() -> adapter.notifyDataSetChanged());*/

        ArrayList<String> moves = currentPokemon.getSMoves();
        RecyclerView rw = findViewById(R.id.recylcerView1);
        CardAdapter ca = new CardAdapter(moves);
        rw.setAdapter(ca);
        rw.setLayoutManager(new LinearLayoutManager(this));

    }

    //TODO: Check API to get all information concerning this pokemon and put items slowly on the activity
    //TODO: Check if there are images from the API
}