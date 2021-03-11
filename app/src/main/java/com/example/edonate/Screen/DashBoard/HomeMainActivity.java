package com.example.edonate.Screen.DashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.edonate.R;
import com.example.edonate.Screen.DashBoard.AddPost.AddPostsFragment;
import com.example.edonate.Screen.DashBoard.Home.HomeFragment;
import com.example.edonate.Screen.DashBoard.Profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        bottomNavigation = findViewById(R.id.nav_view);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        // Passing each menu ID as a set of Ids because each
      //  openFragment(new ProfileFragment());
       openFragment(new HomeFragment());



    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            //Log.d("menu event","Home");
                            openFragment(new HomeFragment());
                            return true;
                        case R.id.navigation_addpost:
                            // Log.d("menu event","Home");
                            openFragment(new AddPostsFragment());
                            return true;
                        case R.id.navigation_profile:
                            // Log.d("menu event","Home");
                       openFragment(new ProfileFragment());

                            return true;
                    }
                    return false;
                }
            };



    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}