package com.example.app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    FrameLayout container;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    InboxFragment inboxFragment = new InboxFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.darker_matcha));
        setContentView(R.layout.dashboard_activity);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        container = findViewById(R.id.container);

        // Setup navigation drawer if available
        if (findViewById(R.id.nav_drawer) != null) {
            navigationView = findViewById(R.id.nav_drawer);

            // Set the burger icon color to black
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_drawerblack);
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.setHomeAsUpIndicator(drawable); // Set the burger icon
            drawerToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable home button

            // Create a ColorStateList programmatically for item text color and icon tint
            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_checked }, // Checked state
                    new int[] { -android.R.attr.state_checked } // Default state
            };

            int[] colors = new int[] {
                    ContextCompat.getColor(this, R.color.matcha), // Color for checked state
                    ContextCompat.getColor(this, R.color.black) // Default color
            };

            ColorStateList colorStateList = new ColorStateList(states, colors);
            navigationView.setItemTextColor(colorStateList);
            navigationView.setItemIconTintList(colorStateList);

            // Set custom background for navigation items
            navigationView.setItemBackgroundResource(R.color.transparent);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    int itemId = item.getItemId();

                    // Handle menu item selection
                    if (itemId == R.id.burgerProfile) {
                        replaceFragment(profileFragment);
                    } else if (itemId == R.id.logout) {
                        // Toast for logging out
                        Toast.makeText(Dashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        // Create an Intent for Login activity
                        intent = new Intent(Dashboard.this, Login.class);
                        startActivity(intent);
                    }

                    // Close the drawer after handling the item
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }

        // Bottom navigation setup
        if (findViewById(R.id.bottomNavigationView) != null) {
            bottomNavigationView = findViewById(R.id.bottomNavigationView);

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.navHome) {
                        replaceFragment(homeFragment);
                        return true;
                    } else if (itemId == R.id.navProfile) {
                        replaceFragment(profileFragment);
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            // Initial fragment or start up fragment
            replaceFragment(homeFragment);
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(container.getId(), fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
