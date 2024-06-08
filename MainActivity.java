package com.example.vivify_technocrats;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
FirebaseAnalytics analytics;
Button button;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    //  FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        analytics = FirebaseAnalytics.getInstance(this);

button = findViewById(R.id.shortsButton);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this,PlayingVideos.class);
        startActivity(intent);
    }
});






        // button for going to next activity
        Button nextActivityButton = findViewById(R.id.nextActivityButton);
        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewActivity.class);

                startActivity(intent);
            }
        });










        //  fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId ==R.id.bottom_home){
                    openFragment(new HomeFragment());
                    return true;
                }else if (itemId == R.id.bottom_call) {
                    // Remove ShortsFragment code
                    // openFragment(new ShortsFragment());
                    // Replace it with your button click action
                    //openShortsActivity(); // Define openShortsActivity method
                    return true;
                }
                else if (itemId == R.id.bottom_email) {
                    openFragment(new EmailFragment());
                    return true;

                }
                else if (itemId == R.id.bottom_map) {
                    openFragment(new MapFragment());
                    return true;

                }
                return false;
            }
        });





        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());
//fab.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Toast.makeText(MainActivity.this, "Upload Videos", Toast.LENGTH_SHORT).show();
//    }
//});

        // Initialize your views
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();



        // Set up onBackPressed callback
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press here
                // For example, you can close the drawer if it's open
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // If drawer is not open, proceed with default back button behavior
//                 onBackPressed();
                }
            }
        };

        // Add callback to the onBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_mail){
            openFragment(new EmailingFragment());
            Toast.makeText(this, "Opening email fragment", Toast.LENGTH_SHORT).show();
        } else if (itemId ==R.id.nav_call) {
            openFragment(new CallingFragment());
            Toast.makeText(this, "Opening calling fragment", Toast.LENGTH_SHORT).show();
        } else if (itemId ==R.id.nav_chat) {
            //  Toast.makeText(this, "Chat", Toast.LENGTH_SHORT).show();
            openFragment(new ChatFragment());
            Toast.makeText(this, "Opening chatting fragment", Toast.LENGTH_SHORT).show();
        } else if (itemId ==R.id.nav_location) {
            //   Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show();
            openFragment(new LocationFragment());
            Toast.makeText(this, "Opening location fragment", Toast.LENGTH_SHORT).show();
        } else if (itemId ==R.id.nav_video) {

            // Toast.makeText(this, "Video", Toast.LENGTH_SHORT).show();
            openFragment(new VideoFragment());
            Toast.makeText(this, "Opening video fragment", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.nav_trending) {
            // Toast.makeText(this, "Trending", Toast.LENGTH_SHORT).show();
            openFragment(new TrendingFragment());
            Toast.makeText(this, "Opening trending fragment", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }












}
