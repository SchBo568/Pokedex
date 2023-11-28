package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        navigationSetup();
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