package com.example.app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

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
        setContentView(R.layout.dashboard_activity);

        //To remove ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setTitle("");
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        container = findViewById(R.id.container);

        // Setup navigation drawer if available
        if (findViewById(R.id.nav_drawer) != null) {
            navigationView = findViewById(R.id.nav_drawer);

            //setting the burger's color to black
            Drawable drawable = ContextCompat.getDrawable(this,R.drawable.ic_drawerblack);
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.setHomeAsUpIndicator(drawable);
            drawerToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable home button

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override //boolean for Burger
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.abtUs || itemId == R.id.support || itemId == R.id.burgerProfile || itemId == R.id.contactUs || itemId == R.id.gmail) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                    return false;
                }
            });
        }

        // boolean for bottom navigation
        if (findViewById(R.id.bottomNavigationView) != null) {
            bottomNavigationView = findViewById(R.id.bottomNavigationView);

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.navHome) {
                        replaceFragment(homeFragment);
                        return true;
                    } else if (itemId == R.id.navMail) {
                        replaceFragment(inboxFragment);
                        return true;
                    } else if (itemId == R.id.navProfile) {
                        replaceFragment(profileFragment);
                        return true;
                    } else {
                        return false;
                    }
                }
            });


            //Badge for Inbox icon
            BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.navMail);
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(1000);

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
